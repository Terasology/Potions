// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.system;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.registry.In;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.component.PotionEffectsListComponent;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * This authority system manages the duration updates of every potion effect modifier in every entity.
 */
@RegisterSystem(value = RegisterMode.AUTHORITY)
public class PotionEffectsAuthoritySystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    /**
     * Integer storing when to check each effect.
     */
    private static final int CHECK_INTERVAL = 100;

    /**
     * Last time the list of regen effects were checked.
     */
    private long lastUpdated;

    @In
    private Time time;
    @In
    private EntityManager entityManager;
    @In
    private PrefabManager prefabManager;

    /**
     * For every update, check to see if the time's been over the CHECK_INTERVAL. If so, subtract the delta time from
     * the remaining duration of each potion effect modifier.
     *
     * @param delta The time (in seconds) since the last engine update.
     */
    @Override
    public void update(float delta) {
        final long currentTime = time.getGameTimeInMs();

        // If the current time passes the CHECK_INTERVAL threshold, continue.
        if (currentTime >= lastUpdated + CHECK_INTERVAL) {
            lastUpdated = currentTime;

            // Iterate through all of the entities that have potions-based effects, and reduce the duration remaining
            // on each (as long as they have a finite amount of time).
            for (EntityRef entity : entityManager.getEntitiesWith(PotionEffectsListComponent.class)) {
                // Get the list of potion effects applied to this entity.
                final PotionEffectsListComponent effectsList = entity.getComponent(PotionEffectsListComponent.class);

                // Search through each type of potion-based AlterationEffects.
                Iterator<Entry<String, PotionEffect>> effectIter = effectsList.effects.entrySet().iterator();
                while (effectIter.hasNext()) {
                    Entry<String, PotionEffect> effect = effectIter.next();

                    effect.getValue().duration -= CHECK_INTERVAL;

                    // If this effect has no remaining time, remove it from the potion effects map.
                    if (effect.getValue().duration <= 0) {
                        effectIter.remove();
                    }
                }
            }
        }
    }
}
