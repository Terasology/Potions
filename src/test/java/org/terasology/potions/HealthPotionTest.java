/*
 * Copyright 2019 MovingBlocks
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

import com.google.api.client.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.players.PlayerCharacterComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.moduletestingenvironment.ModuleTestingEnvironment;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HealthPotionTest extends ModuleTestingEnvironment {
    private EntityManager entityManager;
    private InventoryManager inventoryManager;

    @Override
    public Set<String> getDependencies() {
        Set<String> modules = Sets.newHashSet();
        modules.add("Potions");
        modules.add("Core");
        return modules;
    }

    @Before
    public void initialize() {
        entityManager = getHostContext().get(EntityManager.class);
        inventoryManager = getHostContext().get(InventoryManager.class);
    }

    @Test
    public void healPotionTest() {
        final EntityRef player = entityManager.create();
        player.addComponent(new PlayerCharacterComponent());

        HealthComponent health = new HealthComponent();
        health.currentHealth = 40;
        health.maxHealth = 100;

        player.addComponent(health);

        EntityRef healPotion = entityManager.create("Potions:HealPotion");

        inventoryManager.giveItem(player, player, healPotion);
        ActivateEvent activateEvent = new ActivateEvent(
                healPotion,             // target
                player,                 // instigator
                null,               // origin
                null,           // direction
                Vector3f.zero(),        // hit position
                Vector3f.zero(),        // hit normal
                0            // activation id
        );

        player.send(activateEvent);
        assertEquals(60, player.getComponent(HealthComponent.class).currentHealth);
    }
}
