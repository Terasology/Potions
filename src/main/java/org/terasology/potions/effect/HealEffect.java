/*
 * Copyright 2020 MovingBlocks
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
import org.terasology.potions.HerbEffect;
import org.terasology.logic.health.event.DoRestoreEvent;
import org.terasology.math.TeraMath;

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
        entity.send(new DoRestoreEvent(TeraMath.floorToInt(magnitude), instigator));
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
        entity.send(new DoRestoreEvent(TeraMath.floorToInt(magnitude), instigator));
    }
}
