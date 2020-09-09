// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.effect;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.health.logic.event.DoDamageEvent;
import org.terasology.math.TeraMath;
import org.terasology.potions.HerbEffect;

/**
 * This HerbEffect harms the target entity instantly when applied.
 */
public class HarmEffect implements HerbEffect {
    /**
     * Apply the harming effect onto the entity. This will harm {@code magnitude} HP.
     *
     * @param instigator The instigator who is applying this effect on the entity. It can be a herb, potion,
     *         etc.
     * @param entity The entity who the harming effect is being applied on.
     * @param magnitude The magnitude of the harming effect.
     * @param duration The duration of the harming effect in milliseconds. It's unused here as the harm is
     *         instant.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        entity.send(new DoDamageEvent(TeraMath.floorToInt(magnitude)));
    }

    /**
     * Apply the harming effect onto the entity. This will harm {@code magnitude} HP.
     *
     * @param instigator The instigator who is applying this effect on the entity. It can be a herb, potion,
     *         etc.
     * @param entity The entity who the harming effect is being applied on.
     * @param id Unused.
     * @param magnitude The magnitude of the harming effect.
     * @param duration The duration of the harming effect in milliseconds. It's unused here as the harm is
     *         instant.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {
        entity.send(new DoDamageEvent(TeraMath.floorToInt(magnitude)));
    }
}
