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
package org.terasology.potions.component;

import org.terasology.network.Replicate;
import org.terasology.reflection.MappedContainer;

/** Common class for describing an effect that may be present in a potion. */
@MappedContainer
public class PotionEffect {
    /** Name of the potion's effect. */
    @Replicate
    public String effect;

    /** Magnitude of the potion's effect. */
    @Replicate
    public float magnitude;

    /** Duration of the potion's effect in milliseconds. */
    @Replicate
    public long duration;
}
