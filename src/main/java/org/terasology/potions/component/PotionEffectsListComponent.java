// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This component is used for storing a mapped list of physical stats modifiers that are applied to the entity that
 * this component's attached to. Each map entry is a reference to an item's or effect's physical stat modifiers. This is
 * intended to be attached to entities, not items. Use PhysicalStatsModifierComponent for items.
 *
 * Note: Make sure that the entity you are attaching this to has a PhysicalStatsComponent.
 */
public class PotionEffectsListComponent implements Component<PotionEffectsListComponent> {
    /** A map of potion-based effects being applied to an entity. */
    @Replicate
    public Map<String, PotionEffect> effects = new HashMap<String, PotionEffect>();

    @Override
    public void copyFrom(PotionEffectsListComponent other) {
        effects.clear();
        other.effects.forEach((k, v) -> this.effects.put(k, v.copy()));
    }
}
