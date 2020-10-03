// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.effect;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.explosives.logic.ExplosionActionComponent;
import org.terasology.potions.HerbEffect;

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
