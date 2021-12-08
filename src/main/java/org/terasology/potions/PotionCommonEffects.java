// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions;

/**
 * This class contains a list of constants (effect IDs) used for potion-related effect type checks. Most are
 * self-explanatory as to what they mean.
 */
public final class PotionCommonEffects {
    public static final String DO_NOTHING = "NOTHING";
    public static final String HARM = "HARM";
    public static final String RESIST_PHYSICAL = "RESIST_PHYSICAL";

    public static final String HEAL = "HEAL";
    public static final String REGEN = "REGEN";
    public static final String TEMP_MAX_HEALTH_BOOST = "TEMP_MAX_HEALTH_BOOST";

    public static final String POISON = "POISON";
    public static final String EXPLOSIVE = "EXPLOSIVE";
    public static final String RESIST_POISON = "RESIST_POISON";
    public static final String CURE_POISON = "CURE_POISON";
    public static final String CURE_ALL_AILMENTS = "CURE_ALL_AILMENTS";

    public static final String WALK_SPEED = "WALK";
    public static final String SWIM_SPEED = "SWIM";
    public static final String JUMP_SPEED = "JUMP";
    public static final String ITEM_USE_SPEED = "ITEM_USE_SPEED";
    public static final String GLUE = "GLUE";

    public static final String DAMAGE_REDUCE = "DAMAGE_REDUCE";
    public static final String FEATHER_FALL = "FEATHER_FALL";
    public static final String BRICK_FALL = "BRICK_FALL";
    public static final String MULTI_JUMP = "MULTI_JUMP";

    // TODO: Awaiting API implementation for custom actions in Terasology for the NoVisibilityEffect to work.
    public static final String NO_VISIBILITY = "NO_VISIBILITY";
    public static final String INVERSION_OF_CONTROLS = "INVERSION_OF_CONTROLS";
    
    private PotionCommonEffects() {

    }
}
