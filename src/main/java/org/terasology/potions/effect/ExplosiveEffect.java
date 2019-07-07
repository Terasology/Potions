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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.actions.ExplosionActionComponent;
import org.terasology.potions.HerbEffect;

/**
 * This effect destroys blocks in a predefined block radius (the built-in one for now)
 */
public class ExplosiveEffect implements HerbEffect {
    /**
     *
     *
     * @param instigator    The instigator who is applying this effect on the entity. It can be a herb, potion, etc.
     * @param entity        The entity who the herb effect is being applied on.
     * @param magnitude     The magnitude of the effect.
     * @param duration      The duration of the effect in milliseconds.
     */
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        createExplosion(instigator, entity, magnitude);
    }

    /**
     *
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
        createExplosion(instigator, entity, magnitude);
    }


    private void createExplosion(EntityRef instigator, EntityRef entity, float magnitude) {
        instigator.addComponent(new ExplosionActionComponent());


    }
}
