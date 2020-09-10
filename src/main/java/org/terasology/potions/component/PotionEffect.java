// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.component;

import org.terasology.engine.network.Replicate;
import org.terasology.nui.reflection.MappedContainer;

/**
 * Common class for describing an effect that may be present in a potion.
 */
@MappedContainer
public class PotionEffect {
    /**
     * Name of the potion's effect.
     */
    @Replicate
    public String effect;

    /**
     * Magnitude of the potion's effect.
     */
    @Replicate
    public float magnitude;

    /**
     * Duration of the potion's effect in milliseconds.
     */
    @Replicate
    public long duration;
}
