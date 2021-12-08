// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.effect;

import org.joml.Math;
import org.joml.RoundingMode;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.module.health.events.DoRestoreEvent;
import org.terasology.potions.HerbEffect;

/**
 * This HerbEffect heals the target entity instantly when applied.
 */
public class HealEffect implements HerbEffect {
    /**
     * Apply the healing effect onto the entity. This will heal {@code magnitude} HP.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the healing effect is being applied on.
     * @param magnitude     The magnitude of the healing effect.
     * @param duration      The duration of the healing effect in milliseconds. It's unused here as the heal is instant.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        entity.send(new DoRestoreEvent(Math.roundUsing(magnitude, RoundingMode.FLOOR), instigator));
    }

    /**
     * Apply the healing effect onto the entity. This will heal {@code magnitude} HP.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the healing effect is being applied on.
     * @param id            Unused.
     * @param magnitude     The magnitude of the healing effect.
     * @param duration      The duration of the healing effect in milliseconds. It's unused here as the heal is instant.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {
        entity.send(new DoRestoreEvent(Math.roundUsing(magnitude, RoundingMode.FLOOR), instigator));
    }
}
