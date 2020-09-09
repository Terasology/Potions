// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.system;

import org.terasology.alterationEffects.AlterationEffects;
import org.terasology.alterationEffects.OnEffectModifyEvent;
import org.terasology.alterationEffects.OnEffectRemoveEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.component.PotionEffectsListComponent;
import org.terasology.potions.events.BeforeApplyPotionEffectEvent;

/**
 * This system handles the application and removal of potion-based effects.
 */
@RegisterSystem
public class PotionEffectsSystem extends BaseComponentSystem {

    /**
     * String constant for identifying potion effects.
     */
    private final static String POTION_EFFECT_PREFIX = "POTIONS";

    /**
     * When an alteration effect is going to be applied, add the potion effects of the same type that can contribute to
     * it as modifiers.
     *
     * @param event Event containing information on what alteration effect is being applied, as well as a list
     *         of effect modifiers that can be added to.
     * @param instigator The entity who instigated this event.
     */
    @ReceiveEvent
    public void onEffectApplied(OnEffectModifyEvent event, EntityRef instigator) {
        PotionEffectsListComponent potionEffectsList = instigator.getComponent(PotionEffectsListComponent.class);

        // If there's no list of potion effects, return.
        if (potionEffectsList == null) {
            return;
        }

        // Get the canonical name of the base alteration effect, and use that to get hte potion effect from the list of
        // potion effects present on this entity.
        String name = event.getAlterationEffect().getClass().getCanonicalName();
        PotionEffect potionEffect = potionEffectsList.effects.get(name + event.getId());

        // If this component doesn't exist, return.
        if (potionEffect == null) {
            return;
        }

        // Send out an event so that other systems can affect the values of the potion effect before it's applied.
        BeforeApplyPotionEffectEvent beforeDrink =
                instigator.send(new BeforeApplyPotionEffectEvent(potionEffect,
                        event.getEntity(), event.getInstigator()));

        // If the event has not been consumed.
        if (!beforeDrink.isConsumed()) {
            // Get the modified magnitude and duration from the event, and if the latter is greater than 0, add the
            // modified duration and magnitude to the effect modify event. POTION_EFFECT_PREFIX is used as the effectID
            // so that this system can later recognize and remove the effect.
            float modifiedMagnitude = beforeDrink.getMagnitudeResultValue();
            long modifiedDuration = (long) beforeDrink.getDurationResultValue();

            if (modifiedDuration > 0) {
                event.addDuration(modifiedDuration, POTION_EFFECT_PREFIX);
                event.addMagnitude(modifiedMagnitude);
            }
        }
        // Otherwise, if the event was consumed, remove the potion effect from the potion effects map. After all, if
        // the event was consumed, that means tha application of the potion effect was denied, meaning that it would
        // need to be removed from the active potion effects map.
        else {
            potionEffectsList.effects.remove(name + event.getId());
            instigator.saveComponent(potionEffectsList);
        }
    }

    /**
     * When an effect modifier is going to be removed, first see if it was added in this system (or module). If so,
     * remove it from the potion effects map. Otherwise, leave it alone as it was added by another system. Remember, the
     * entire alteration effect may or may not be removed following this. It depends on whether there are any remaining
     * effect modifiers that contribute to the base alteration effect.
     *
     * @param event Event containing information on what was the alteration effect being applied, and some
     *         details on the effect modifier expiring.
     * @param instigator The entity who instigated this event.
     */
    @ReceiveEvent
    public void onEffectRemoved(OnEffectRemoveEvent event, EntityRef instigator) {
        // This is used as a guard/delimiter so that removal event intended for other systems will not affect this one.
        if (!event.getEffectId().equals(POTION_EFFECT_PREFIX)
                && !event.getEffectId().equals(AlterationEffects.CONSUMABLE_ITEM)) {
            return;
        }

        PotionEffectsListComponent potionEffectsList = instigator.getComponent(PotionEffectsListComponent.class);

        // If there's no list of potion effects, return.
        if (potionEffectsList == null) {
            return;
        }

        // Remove the potion effect described in the event information from the potion effects map.
        potionEffectsList.effects.remove(event.getAlterationEffect().getClass().getCanonicalName() + event.getId());
    }
}
