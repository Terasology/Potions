// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.component;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

import java.util.List;

/**
 * This component is used for storing information about a potion. Specifically, what potion bottle it came from, the
 * durability cost per drink, whether it has a genome, and all of the potion's effects.
 */
public final class PotionComponent implements Component {
    /**
     * Can this potion bottle be reused indefinitely.
     */
    @Replicate
    public boolean hasInfDurability = false;

    /**
     * Name of the empty bottle prefab.
     */
    @Replicate
    public String bottlePrefab = "Potions:GlassBottle";

    /**
     * What's the durability cost per drink.
     */
    @Replicate
    public int costPerDrink = 3;

    /**
     * Flag for storing whether this potion has a genome. If a potion has been predefined by a developer, set this to
     * false.
     */
    public boolean hasGenome = true;

    /**
     * List of PotionEffects that this potion has.
     */
    @Replicate
    public List<PotionEffect> effects = Lists.newArrayList();
}