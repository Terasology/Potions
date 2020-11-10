// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.component;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

import java.util.HashMap;
import java.util.Map;

/**
 * This component is used for storing a mapped list of physical stats modifiers that are applied to the entity that
 * this component's attached to. Each map entry is a reference to an item's or effect's physical stat modifiers. This is
 * intended to be attached to entities, not items. Use PhysicalStatsModifierComponent for items.
 *
 * Note: Make sure that the entity you are attaching this to has a PhysicalStatsComponent.
 */
public class PotionEffectsListComponent implements Component {
    /** A map of potion-based effects being applied to an entity. */
    @Replicate
    public Map<String, PotionEffect> effects = new HashMap<String, PotionEffect>();
}
