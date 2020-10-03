// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.effect;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.potions.HerbEffect;

/**
 * This HerbEffect does absolutely nothing when applied to an entity. This is used as a fail-safe in several locations
 * in case one or more of the listed effects in prefabs are invalid.
 */
public class DoNothingEffect implements HerbEffect {
    /**
     * Do nothing when attempting to apply an effect to the given entity.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param magnitude     The magnitude of the effect.
     * @param duration      The duration of the effect in milliseconds.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
    }

    /**
     * Do nothing when attempting to apply an effect to the given entity.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param id            This particular HerbEffect's ID. Can be used to differentiate different effects under the
     *                      same family.
     * @param magnitude     The magnitude of the effect.
     * @param duration      The duration of the effect in milliseconds.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {
    }
}
