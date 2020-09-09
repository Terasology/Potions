// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions;

import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 * Interface for the application of various herb effects over a period of time.
 */
public interface HerbEffect {
    /**
     * Apply an herb effect on the entity for the given magnitude and duration.
     *
     * @param instigator The instigator who is applying this effect on the entity. It can be a herb, potion,
     *         etc.
     * @param entity The entity who the herb effect is being applied on.
     * @param magnitude The magnitude of the effect.
     * @param duration The duration of the effect in milliseconds.
     */
    void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration);

    /**
     * Apply an herb effect with a specific ID on the entity for the given magnitude and duration. This specific ID can
     * be used to differentiate effects, such as the different types of damage over time effects. It's up to the
     * individual HerbEffect implementations to decide how to use this.
     *
     * @param instigator The instigator who is applying this effect on the entity. It can be a herb, potion,
     *         etc.
     * @param entity The entity who the herb effect is being applied on.
     * @param id This particular HerbEffect's ID. Can be used to differentiate different effects under the same
     *         family.
     * @param magnitude The magnitude of the effect.
     * @param duration The duration of the effect in milliseconds.
     */
    void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration);
}
