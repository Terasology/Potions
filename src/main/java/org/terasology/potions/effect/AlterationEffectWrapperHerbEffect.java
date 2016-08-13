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

public class AlterationEffectWrapperHerbEffect implements HerbEffect {
    private AlterationEffect alterationEffect;
    private float durationMultiplier;
    private float magnitudeMultiplier;
    private String effectID;

    public AlterationEffectWrapperHerbEffect(AlterationEffect alterationEffect, float durationMultiplier, float magnitudeMultiplier) {
        this.alterationEffect = alterationEffect;
        this.durationMultiplier = durationMultiplier;
        this.magnitudeMultiplier = magnitudeMultiplier;
    }

    public AlterationEffectWrapperHerbEffect(AlterationEffect alterationEffect, String effectID,
                                             float durationMultiplier, float magnitudeMultiplier) {
        this.alterationEffect = alterationEffect;
        this.effectID = effectID;
        this.durationMultiplier = durationMultiplier;
        this.magnitudeMultiplier = magnitudeMultiplier;
    }

    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        alterationEffect.applyEffect(instigator, entity, magnitude * magnitudeMultiplier, (long) (duration * durationMultiplier));
    }

    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {
        alterationEffect.applyEffect(instigator, entity, id, magnitude * magnitudeMultiplier, (long) (duration * durationMultiplier));
    }
}
