/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.potions.system;

import org.terasology.alterationEffects.OnEffectModifyEvent;
import org.terasology.alterationEffects.OnEffectRemoveEvent;
import org.terasology.context.Context;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.component.PotionEffectsListComponent;
import org.terasology.potions.events.BeforeApplyPotionEffectEvent;
import org.terasology.registry.In;

@RegisterSystem
public class PotionEffectsSystem extends BaseComponentSystem {

    private final static String POTION_EFFECT_PREFIX = "POTIONS";

    @ReceiveEvent
    public void onEffectApplied(OnEffectModifyEvent event, EntityRef instigator) {
        // So, a PotionEffectsListCompoent (analogue to EquipmentEffectsListComponent.
        PotionEffectsListComponent potionEffectsList = instigator.getComponent(PotionEffectsListComponent.class);

        if (potionEffectsList == null) {
            return;
        }

        String name = event.getAlterationEffect().getClass().getCanonicalName();
        PotionEffect potionEffect = potionEffectsList.effects.get(name + event.getId());

        if (potionEffect == null) {
            return;
        }

        BeforeApplyPotionEffectEvent beforeDrink =
                instigator.send(new BeforeApplyPotionEffectEvent(potionEffect,
                        event.getEntity(), event.getInstigator()));

        if (!beforeDrink.isConsumed()) {
            float modifiedMagnitude = beforeDrink.getMagnitudeResultValue();
            long modifiedDuration = (long) beforeDrink.getDurationResultValue();
            if (/*modifiedMagnitude > 0 &&*/ modifiedDuration > 0) {
                event.addDuration(modifiedDuration, POTION_EFFECT_PREFIX);
                event.addMagnitude(modifiedMagnitude);
            }
        }
    }

    @ReceiveEvent
    public void onEffectRemoved(OnEffectRemoveEvent event, EntityRef instigator) {
        // This is used as a guard/delimiter so remove events intended for other systems will not affect this one.
        if (!event.getEffectId().equals(POTION_EFFECT_PREFIX)) {
            return;
        }

        PotionEffectsListComponent potionEffectsList = instigator.getComponent(PotionEffectsListComponent.class);

        if (potionEffectsList == null) {
            return;
        }

        // TODO: Need to add event.getEffectId() so that
        potionEffectsList.effects.remove(event.getAlterationEffect().getClass().getCanonicalName() + event.getId());
    }
}
