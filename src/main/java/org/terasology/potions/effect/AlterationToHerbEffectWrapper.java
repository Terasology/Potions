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

import org.terasology.alterationEffects.AlterationEffect;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.potions.HerbEffect;

/**
 * This HerbEffect wraps any AlterationEffect so that the latter can function within the herbalism systems and be treated
 * as a HerbEffect.
 */
public class AlterationToHerbEffectWrapper implements HerbEffect {
    /** The AlterationEffect being wrapped. */
    private AlterationEffect alterationEffect;

    /** Multiplies the duration of the effect. */
    private float durationMultiplier;

    /** Multiplies the magnitude of the effect. */
    private float magnitudeMultiplier;

    /** The specific ID of this effect. */
    private String effectID;

    /**
     * Construct an instance of this wrapper using the given parameters. It can then be treated like any other HerbEffect
     * when calling the applyEffect methods.
     *
     * @param alterationEffect      The AlterationEffect to be wrapped.
     * @param durationMultiplier    The value by which any duration should be multiplied by.
     * @param magnitudeMultiplier   The value by which any magnitude should be multiplied by.
     */
    public AlterationToHerbEffectWrapper(AlterationEffect alterationEffect, float durationMultiplier, float magnitudeMultiplier) {
        this.alterationEffect = alterationEffect;
        this.durationMultiplier = durationMultiplier;
        this.magnitudeMultiplier = magnitudeMultiplier;
    }

    /**
     * Construct an instance of this wrapper using the given parameters. It can then be treated like any other HerbEffect
     * when calling the applyEffect methods.
     *
     * @param alterationEffect      The AlterationEffect to be wrapped.
     * @param effectID              A specific ID to differentiate this effect from other effects in the same family.
     * @param durationMultiplier    The value by which any duration should be multiplied by.
     * @param magnitudeMultiplier   The value by which any magnitude should be multiplied by.
     */
    public AlterationToHerbEffectWrapper(AlterationEffect alterationEffect, String effectID,
                                         float durationMultiplier, float magnitudeMultiplier) {
        this.alterationEffect = alterationEffect;
        this.effectID = effectID;
        this.durationMultiplier = durationMultiplier;
        this.magnitudeMultiplier = magnitudeMultiplier;
    }

    /**
     * Apply the wrapped alteration effect on the entity for the given magnitude and duration. Note that these two values
     * will be multiplied by magnitudeMultiplier and durationMultiplier respectively.
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param magnitude     The magnitude of the effect.
     * @param duration      The duration of the effect in milliseconds.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        alterationEffect.applyEffect(instigator, entity, magnitude * magnitudeMultiplier, (long) (duration * durationMultiplier));
    }

    /**
     * Apply the wrapped alteration effect with a specific ID on the entity for the given magnitude and duration.
     * Note that these two values will be multiplied by magnitudeMultiplier and durationMultiplier respectively.
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
        alterationEffect.applyEffect(instigator, entity, id, magnitude * magnitudeMultiplier, (long) (duration * durationMultiplier));
    }

    public AlterationEffect getAlterationEffect() {
        return alterationEffect;
    }
}
