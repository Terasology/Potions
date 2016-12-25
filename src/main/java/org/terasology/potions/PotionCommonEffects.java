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
