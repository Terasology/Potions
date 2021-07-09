// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0


package org.terasology.potions.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.EmptyComponent;

/**
 * This component is only used as an identifier to identify if the entity is an empty potion container.
 */
@Replicate
public final class EmptyPotionComponent extends EmptyComponent<EmptyPotionComponent> {
}
