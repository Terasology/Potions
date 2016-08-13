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
import org.terasology.potions.HerbEffect;
import org.terasology.logic.health.DoHealEvent;
import org.terasology.math.TeraMath;

public class HealEffect implements HerbEffect {
    private int maxHeal = 100;

    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        entity.send(new DoHealEvent(TeraMath.floorToInt(maxHeal * magnitude), instigator));
    }

    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {
        entity.send(new DoHealEvent(TeraMath.floorToInt(maxHeal * magnitude), instigator));
    }
}
