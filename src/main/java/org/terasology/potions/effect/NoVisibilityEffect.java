// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.effect;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.potions.HerbEffect;

public class NoVisibilityEffect implements HerbEffect {
    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, float magnitude, long duration) {
        // TODO: Once the API for Custom Actions is implemented, the code for NoVisibility Effect goes here.
        // The effect will make the screen blank for the given amount of time.
    }

    @Override
    public void applyEffect(EntityRef instigator, EntityRef entity, String id, float magnitude, long duration) {

    }
}
