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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.binds.inventory.InventoryButton;
import org.terasology.logic.delay.DelayManager;
import org.terasology.logic.delay.PeriodicActionTriggeredEvent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.events.DrinkPotionEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

import java.util.ArrayList;


@RegisterSystem(RegisterMode.CLIENT)
public class PotionStatusUISystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(PotionStatusUISystem.class);

    @In
    private NUIManager nuiManager;
    @In
    private DelayManager delayManager;
    @In
    private EntityManager entityManager;

    private PotionStatusScreen screen;

    private ArrayList<Long> durations = new ArrayList<>();
    private ArrayList<String> effects = new ArrayList<>();

    private boolean isScreenVisible = false;

    @Override
    public void initialise() {
    }

    @ReceiveEvent(priority = 110)
    public void inventoryToggled(InventoryButton event, EntityRef entity) {

        logger.info("inv toggle: " + entity.toString());
        if (event.getState() == ButtonState.DOWN) {
            nuiManager.toggleScreen("potions:potionStatusScreen");
            isScreenVisible = nuiManager.isOpen("potions:potionStatusScreen");
            updateScreen();
        }
    }

    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void onPotionDrink(DrinkPotionEvent event, EntityRef entity) {

        logger.info("drink: " + entity.toString());
        for (PotionEffect effect : event.getPotionComponent().effects) {
            int index = effects.indexOf(effect);
            if (index == -1) {
                effects.add(effect.effect);
                durations.add(effect.duration);
                delayManager.addPeriodicAction(entity, "PUI" + effect.effect, 500, 500);
            } else {
                effects.set(index, effect.effect);
                durations.set(index, effect.duration);
            }
        }
        updateScreen();
    }

    @ReceiveEvent
    public void onEffectUpdate(PeriodicActionTriggeredEvent event, EntityRef entity) {
        logger.info("Periodic Activated");
        String actionID = event.getActionId();
        if (actionID.startsWith("PUI")) {
            logger.info(actionID.substring(3));
            int index = effects.indexOf(actionID.substring(3));
            durations.set(index, durations.get(index) - 500);
            if (durations.get(index) < 0) {
                durations.remove(index);
                effects.remove(index);
                delayManager.cancelPeriodicAction(entity, actionID);
            }
            updateScreen();
        }
    }

    private void updateScreen() {
        if (isScreenVisible) {
            screen = (PotionStatusScreen) nuiManager.getScreen("potions:potionStatusScreen");
            screen.removeAll();
            for (int i = 0; i < Math.min(10, effects.size()); i++) {
                screen.addEffect(i, effects.get(i), durations.get(i));
            }
        }
    }
}
