// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.system;

import org.terasology.alterationEffects.boost.HealthBoostAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.CureAllDamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.CureDamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.DamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.regenerate.RegenerationAlterationEffect;
import org.terasology.alterationEffects.resist.ResistDamageAlterationEffect;
import org.terasology.alterationEffects.speed.GlueAlterationEffect;
import org.terasology.alterationEffects.speed.ItemUseSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.JumpSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.MultiJumpAlterationEffect;
import org.terasology.alterationEffects.speed.SwimSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.WalkSpeedAlterationEffect;
import org.terasology.durability.components.DurabilityComponent;
import org.terasology.durability.events.ReduceDurabilityEvent;
import org.terasology.engine.audio.AudioManager;
import org.terasology.engine.context.Context;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.utilities.Assets;
import org.terasology.inventory.logic.InventoryManager;
import org.terasology.potions.HerbEffect;
import org.terasology.potions.PotionCommonEffects;
import org.terasology.potions.component.PotionComponent;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.component.PotionEffectsListComponent;
import org.terasology.potions.effect.AlterationToHerbEffectWrapper;
import org.terasology.potions.effect.DoNothingEffect;
import org.terasology.potions.effect.ExplosiveEffect;
import org.terasology.potions.effect.HarmEffect;
import org.terasology.potions.effect.HealEffect;
import org.terasology.potions.effect.NoVisibilityEffect;
import org.terasology.potions.events.DrinkPotionEvent;

/**
 * This authority system handles potion consumption by various entities.
 */
@RegisterSystem(value = RegisterMode.AUTHORITY)
public class DrinkPotionAuthoritySystem extends BaseComponentSystem {
    /**
     * Used for playing drinking sounds.
     */
    @In
    private AudioManager audioManager;

    /**
     * This Context is necessary for all the AlterationEffects due to timing and use of the DelayManager.
     */
    @In
    private Context context;

    /**
     * Apply the potion effect with the given ID to the instigator. This will also add the specific effect to the
     * instigator's potion effects map. This calls the {@link #applyPotionEffect(EntityRef, EntityRef, PotionComponent,
     * HerbEffect, PotionEffect, String)}} method but leaves the id field empty.
     *
     * @param instigator The entity who is trying to consume this potion.
     * @param item A reference to the potion item entity which has the potion effect.
     * @param potion The PotionComponent of the potion item.
     * @param herbEffect The HerbEffect that will be used to apply the PotionEffect on the instigator.
     * @param potionEffect The PotionEffect that will be applied onto the instigator.
     */
    private void applyPotionEffect(EntityRef instigator, EntityRef item, PotionComponent potion, HerbEffect herbEffect,
                                   PotionEffect potionEffect) {
        applyPotionEffect(instigator, item, potion, herbEffect, potionEffect, "");
    }

    /**
     * Apply the potion effect with the given ID to the instigator. This will also add the specific effect to the
     * instigator's potion effects map.
     *
     * @param instigator The entity who is trying to consume this potion and have the potion effect applied on
     *         it.
     * @param item A reference to the potion item entity which has the potion effect.
     * @param potion The PotionComponent of the potion item.
     * @param herbEffect The HerbEffect that will be used to apply the PotionEffect on the instigator.
     * @param potionEffect The PotionEffect that will be applied onto the instigator.
     * @param id The ID of this particular HerbEffect. Used to differentiate effects under the same family.
     */
    private void applyPotionEffect(EntityRef instigator, EntityRef item, PotionComponent potion, HerbEffect herbEffect,
                                   PotionEffect potionEffect, String id) {
        // If the instigator doesn't have a potion effects list, create one.
        if (!instigator.hasComponent(PotionEffectsListComponent.class)) {
            instigator.addComponent(new PotionEffectsListComponent());
        }

        PotionEffectsListComponent potionEffectsList = instigator.getComponent(PotionEffectsListComponent.class);

        /*
        The following if-blocks are used for creating the name to store this potion effect under in the potion effects
        map. This is analogous to the effectIDs. The names are defined using these rules:

        * Use the canonical name of the alteration effect class if the HerbEffect is an AlterationToHerbEffectWrapper.
        * Use the prefix "Explosive" if the HerbEffect is an ExplosiveEffect.
        * Use the prefix "Harm" if the HerbEffect is a HarmEffect.
        * Use the prefix "Heal" if the HerbEffect is a HealEffect.
        */
        String name = "";
        if (herbEffect instanceof AlterationToHerbEffectWrapper) {
            name += ((AlterationToHerbEffectWrapper) herbEffect).getAlterationEffect().getClass().getCanonicalName();
        } else if (herbEffect instanceof ExplosiveEffect) {
            name += "Explosive";
        } else if (herbEffect instanceof HarmEffect) {
            name += "Harm";
        } else if (herbEffect instanceof HealEffect) {
            name += "Heal";
        }

        // Add the effect to the potion effects map, and save component storing the map.
        potionEffectsList.effects.put(name + id, potionEffect);
        instigator.saveComponent(potionEffectsList);

        // Apply the herb effect onto the instigator.
        if (id.equalsIgnoreCase("")) {
            herbEffect.applyEffect(item, instigator, potionEffect.effect, potionEffect.magnitude,
                    potionEffect.duration);
        } else {
            herbEffect.applyEffect(item, instigator, id, potionEffect.magnitude, potionEffect.duration);
        }
    }

    /**
     * Event handler that handles consuming a potion without a genome component attached to it. This method will cycle
     * through the potion's list of PotionEffects, apply them to the instigator entity, and then decrement the potion
     * bottle's durability.
     *
     * @param event The DrinkPotionEvent with information about the instigator and the potion.
     * @param ref Unused reference to entity.
     */
    @ReceiveEvent
    public void onPotionWithoutGenomeConsumed(DrinkPotionEvent event, EntityRef ref) {
        // Get the potion item's potion component.
        PotionComponent potion = event.getPotionComponent();
        HerbEffect herbEffect = null;
        String effectID = "";

        // If this potion is supposed to have a dynamically-set Genome, return.
        if (potion.hasGenome) {
            return;
        }

        // If there are no effects, just play the drink sound and return.
        if (potion.effects.size() == 0) {
            audioManager.playSound(Assets.getSound("engine:drink").get(), 1.0f);
            return;
        }

        // Iterate through all effects of this potion and apply them.
        for (PotionEffect pEffect : potion.effects) {
            // herbEffect will store a reference to the HerbEffect, and effectID will store the ID of the effect
            // (if any).
            herbEffect = null;
            effectID = "";

            // Figure out what specific effect this is and create a HerbEffect based on that.
            switch (pEffect.effect) {
                case PotionCommonEffects.HARM:
                    herbEffect = new HarmEffect();
                    break;
                case PotionCommonEffects.HEAL:
                    herbEffect = new HealEffect();
                    break;
                case PotionCommonEffects.REGEN:
                    RegenerationAlterationEffect effect = new RegenerationAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(effect, 1f, 1f);
                    break;
                case PotionCommonEffects.RESIST_PHYSICAL:
                    ResistDamageAlterationEffect resistDamageEffect = new ResistDamageAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(resistDamageEffect, 1f, 1f);
                    effectID = "physicalDamage";
                    break;
                case PotionCommonEffects.WALK_SPEED:
                    WalkSpeedAlterationEffect wsEffect = new WalkSpeedAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(wsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.SWIM_SPEED:
                    SwimSpeedAlterationEffect ssEffect = new SwimSpeedAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(ssEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.JUMP_SPEED:
                    JumpSpeedAlterationEffect jsEffect = new JumpSpeedAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(jsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.ITEM_USE_SPEED:
                    ItemUseSpeedAlterationEffect itsEffect = new ItemUseSpeedAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(itsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.MULTI_JUMP:
                    MultiJumpAlterationEffect mjEffect = new MultiJumpAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(mjEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.POISON:
                    DamageOverTimeAlterationEffect poisonEffect = new DamageOverTimeAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(poisonEffect, "PoisonPotion", 1f, 1f);
                    effectID = "PoisonPotion";
                    break;
                case PotionCommonEffects.RESIST_POISON:
                    ResistDamageAlterationEffect resistDamageEffect2 = new ResistDamageAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(resistDamageEffect2, 1f, 1f);
                    effectID = "poisonDamage";
                    break;
                case PotionCommonEffects.CURE_POISON:
                    CureDamageOverTimeAlterationEffect cureEffect = new CureDamageOverTimeAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(cureEffect, 1f, 1f);
                    effectID = "PoisonPotion";
                    break;
                case PotionCommonEffects.CURE_ALL_AILMENTS:
                    CureAllDamageOverTimeAlterationEffect cureAllEffect =
                            new CureAllDamageOverTimeAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(cureAllEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.TEMP_MAX_HEALTH_BOOST:
                    HealthBoostAlterationEffect hBoostEffect = new HealthBoostAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(hBoostEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.GLUE:
                    GlueAlterationEffect glueEffect = new GlueAlterationEffect(context);
                    herbEffect = new AlterationToHerbEffectWrapper(glueEffect, 1f, 1f);
                    effectID = "GluePotion";
                    break;
                case PotionCommonEffects.EXPLOSIVE:
                    herbEffect = new ExplosiveEffect();
                    break;
                case PotionCommonEffects.NO_VISIBILITY:
                    herbEffect = new NoVisibilityEffect();
                    break;
                default:
                    herbEffect = new DoNothingEffect();
                    break;
            }

            // Apply the potion's current effect onto the instigator.
            applyPotionEffect(event.getInstigator(), event.getItem(), potion, herbEffect, pEffect, effectID);
        }

        // Play the potion drink sound.
        audioManager.playSound(Assets.getSound("engine:drink").get(), 1.0f);

        // Get the EntityRef of the potion item, and its durability component (if any).
        EntityRef item = event.getItem();
        DurabilityComponent durability = item.getComponent(DurabilityComponent.class);

        if (durability != null) {
            // If the new durability value will be above 0 following the potion drink, or the bottle has inf durability,
            // proceed. Otherwise, continue normally by destroying the old potion bottle.
            int newDurabilityValue = durability.durability - potion.costPerDrink;
            if (newDurabilityValue > 0 || potion.hasInfDurability) {
                // Create an empty potion bottle using the bottlePrefab name of the item's potion component.
                EntityRef emptyPotionBottle =
                        CoreRegistry.get(EntityManager.class).create(Assets.getPrefab(potion.bottlePrefab).get());

                // Copy the old durability values from the filled potion bottle to the empty one.
                emptyPotionBottle.getComponent(DurabilityComponent.class).durability = durability.durability;
                emptyPotionBottle.getComponent(DurabilityComponent.class).maxDurability = durability.maxDurability;

                // Send an event to reduce the durability of this potion bottle only if the bottle is unimmune to potion
                // degredation effects.
                if (!potion.hasInfDurability) {
                    emptyPotionBottle.send(new ReduceDurabilityEvent(potion.costPerDrink));
                }

                // Give the empty potion bottle to the player's inventory. This will act as a swap between the filled
                // and
                // empty ones.
                CoreRegistry.get(InventoryManager.class).giveItem(event.getInstigator(), event.getInstigator(),
                        emptyPotionBottle);
            }
        }
    }

    /**
     * Upon activating/using an item with a potion component, send a DrinkPotionEvent to begin drinking the potion.
     *
     * @param event The ActiveEvent which was caught by this handler. The important part of this is the
     *         instigator.
     * @param item Reference to the potion item.
     * @param potion The potion component of the potion item. Used to filter out non-potion item uses.
     */
    @ReceiveEvent
    public void potionWithoutGenomeConsumed(ActivateEvent event, EntityRef item, PotionComponent potion) {
        PotionComponent p = item.getComponent(PotionComponent.class);
        event.getInstigator().send(new DrinkPotionEvent(p, event.getInstigator(), item));
    }
}
