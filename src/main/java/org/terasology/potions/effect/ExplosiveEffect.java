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
package org.terasology.potions.effect;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.logic.actions.ActionTarget;
import org.terasology.logic.actions.ExplosionActionComponent;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.potions.HerbEffect;
import org.terasology.potions.events.DrinkPotionEvent;
import org.terasology.world.block.Block;

/**
 * This effect destroys blocks in a predefined block radius (the built-in one for now)
 */
public class ExplosiveEffect implements HerbEffect {
    /**
     * Apply the explosive effect onto the entity. This will deal {@code magnitude} damage of the explosive damage type
     * over a max range of {@code maxRange} units. It will not (directly) harm the user.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param magnitude     The magnitude of the effect.
     * @param maxRange      The max range of the effect in terms of number of blocks.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long maxRange) {
        createExplosion(instigator, entity, magnitude, maxRange);
    }

    /**
     * Apply the explosive effect onto the entity. This will deal {@code magnitude} damage of the explosive damage type
     * over a max range of {@code maxRange} units. It will not (directly) harm the user.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param id            This particular HerbEffect's ID. Can be used to differentiate different effects under the
     *                      same family.
     * @param magnitude     The magnitude of the effect.
     * @param maxRange      The max range of the effect in terms of number of blocks.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long maxRange) {
        createExplosion(instigator, entity, magnitude, maxRange);
    }

    /**
     * Create explosion originating from the consumer entity.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity or user who this effect explosive effect will be applied on.
     * @param magnitude     The amount of damage this explosion will deal.
     * @param maxRange      The max range of the explosion in blocks.
     */
    private void createExplosion(EntityRef instigator, EntityRef entity, float magnitude, long maxRange) {
        ExplosionActionComponent e = new ExplosionActionComponent();
        e.damageAmount = (int) magnitude;
        e.maxRange = (int) maxRange;
        instigator.addComponent(e);
    }
}
