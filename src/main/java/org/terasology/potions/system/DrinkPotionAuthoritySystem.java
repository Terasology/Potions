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

import org.terasology.alterationEffects.boost.HealthBoostAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.CureAllDamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.CureDamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.damageOverTime.DamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.regenerate.RegenerationAlterationEffect;
import org.terasology.alterationEffects.resist.ResistDamageAlterationEffect;
import org.terasology.alterationEffects.speed.ItemUseSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.JumpSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.MultiJumpAlterationEffect;
import org.terasology.alterationEffects.speed.SwimSpeedAlterationEffect;
import org.terasology.alterationEffects.speed.WalkSpeedAlterationEffect;
import org.terasology.audio.AudioManager;
import org.terasology.context.Context;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.potions.HerbEffect;
import org.terasology.potions.Herbalism;
import org.terasology.potions.PotionCommonEffects;
import org.terasology.potions.component.PotionComponent;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.effect.AlterationEffectWrapperHerbEffect;
import org.terasology.potions.effect.DoNothingEffect;
import org.terasology.potions.effect.HealEffect;
import org.terasology.potions.events.BeforeDrinkPotionEvent;
import org.terasology.potions.events.DrinkPotionEvent;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.registry.In;
import org.terasology.utilities.Assets;

@RegisterSystem(value = RegisterMode.AUTHORITY)
public class DrinkPotionAuthoritySystem extends BaseComponentSystem {
    @In
    private AudioManager audioManager;

    @In
    private Context context;

    private void checkDrink(EntityRef instigator, EntityRef item, PotionComponent p, HerbEffect h, PotionEffect v) {
        checkDrink(instigator, item, p, h, v, "");
    }

    private void checkDrink(EntityRef instigator, EntityRef item, PotionComponent p, HerbEffect h, PotionEffect v, String id) {
        BeforeDrinkPotionEvent beforeDrink = instigator.send(new BeforeDrinkPotionEvent(p, h, v, instigator, item));

        if (!beforeDrink.isConsumed()) {
            float modifiedMagnitude = beforeDrink.getMagnitudeResultValue();
            long modifiedDuration = (long) beforeDrink.getDurationResultValue();

            if (modifiedMagnitude > 0 && modifiedDuration > 0) {
                if (id.equalsIgnoreCase("")) {
                    h.applyEffect(item, instigator, v.effect, modifiedMagnitude, modifiedDuration);
                } else {
                    h.applyEffect(item, instigator, id, modifiedMagnitude, modifiedDuration);
                }
            }
        }
    }

    @ReceiveEvent
    public void onPotionWithoutGenomeConsumed(DrinkPotionEvent event, EntityRef ref) {
        PotionComponent p = event.getPotionComponent();
        HerbEffect e = null;
        String effectID = "";

        // If this potion is supposed to have a dynamically-set Genome, return.
        if (p.hasGenome) {
            return;
        }

        EntityRef item = event.getItem();

        // If there are no effects, just play the drink sound and return.
        if (p.effects.size() == 0) {
            audioManager.playSound(Assets.getSound("engine:drink").get(), 1.0f);
            return;
        }

        // Iterate through all effects of this potion and apply them.
        for (PotionEffect pEffect : p.effects) {
            e = null;
            effectID = "";

            // Figure out what specific effect this is and create a HerbEffect based on that.
            switch (pEffect.effect) {
                case PotionCommonEffects.HEAL:
                    e = new HealEffect();
                    break;
                case PotionCommonEffects.REGEN:
                    RegenerationAlterationEffect effect = new RegenerationAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(effect, 1f, 1f);
                    break;
                case PotionCommonEffects.RESIST_PHYSICAL:
                    ResistDamageAlterationEffect resistDamageEffect = new ResistDamageAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(resistDamageEffect, 1f, 1f);
                    effectID = "physicalDamage";
                    break;
                case PotionCommonEffects.WALK_SPEED:
                    WalkSpeedAlterationEffect wsEffect = new WalkSpeedAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(wsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.SWIM_SPEED:
                    SwimSpeedAlterationEffect ssEffect = new SwimSpeedAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(ssEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.JUMP_SPEED:
                    JumpSpeedAlterationEffect jsEffect = new JumpSpeedAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(jsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.ITEM_USE_SPEED:
                    ItemUseSpeedAlterationEffect itsEffect = new ItemUseSpeedAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(itsEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.MULTI_JUMP:
                    MultiJumpAlterationEffect mjEffect = new MultiJumpAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(mjEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.POISON:
                    DamageOverTimeAlterationEffect poisonEffect = new DamageOverTimeAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(poisonEffect, "PoisonPotion", 1f, 1f);
                    effectID = "PoisonPotion";
                    break;
                case PotionCommonEffects.RESIST_POISON:
                    ResistDamageAlterationEffect resistDamageEffect2 = new ResistDamageAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(resistDamageEffect2, 1f, 1f);
                    effectID = "poisonDamage";
                    break;
                case PotionCommonEffects.CURE_POISON:
                    CureDamageOverTimeAlterationEffect cureEffect = new CureDamageOverTimeAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(cureEffect, 1f, 1f);
                    effectID = "PoisonPotion";
                    break;
                case PotionCommonEffects.CURE_ALL_AILMENTS:
                    CureAllDamageOverTimeAlterationEffect cureAllEffect = new CureAllDamageOverTimeAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(cureAllEffect, 1f, 1f);
                    break;
                case PotionCommonEffects.TEMP_MAX_HEALTH_BOOST:
                    HealthBoostAlterationEffect hBoostEffect = new HealthBoostAlterationEffect(context);
                    e = new AlterationEffectWrapperHerbEffect(hBoostEffect, 1f, 1f);
                    break;
                default:
                    e = new DoNothingEffect();
                    break;
            }

            checkDrink(event.getInstigator(), event.getItem(), p, e, pEffect, effectID);
        }

        audioManager.playSound(Assets.getSound("engine:drink").get(), 1.0f);
    }

    // Consume a potion without a Genome attached to it. Usually predefined ones.
    @ReceiveEvent
    public void potionWithoutGenomeConsumed(ActivateEvent event, EntityRef item, PotionComponent potion) {
        PotionComponent p = item.getComponent(PotionComponent.class);
        event.getInstigator().send(new DrinkPotionEvent(p, event.getInstigator(), item));
    }
}
