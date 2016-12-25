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
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.events.DrinkPotionEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;


@RegisterSystem(RegisterMode.CLIENT)
public class PotionStatusUISystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(PotionStatusUISystem.class);

    @In
    private NUIManager nuiManager;

    @In
    private DelayManager delayManager;

    private PotionStatusScreen screen;

    private long[] durations = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private PotionEffect[] effects = new PotionEffect[10];
    private boolean isScreenVisible = false;

    @ReceiveEvent(priority = 110)
    public void inventoryToggled(InventoryButton event, EntityRef entity) {
        if (event.getState() == ButtonState.DOWN) {
            nuiManager.toggleScreen("potions:potionStatusScreen");
            isScreenVisible = nuiManager.isOpen("potions:potionStatusScreen");
            if (isScreenVisible) {
                screen = (PotionStatusScreen) nuiManager.getScreen("potions:potionStatusScreen");
                for (int i = 0; i < 10; i++) {
                    if (effects[i] != null) {
                        screen.addEffect(i, effects[i].effect, durations[i]);
                    } else {
                        screen.removeEffect(i);
                    }
                }
            }
        }
    }

    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void onPotionDrink(DrinkPotionEvent event, EntityRef entity) {
        logger.info(event.toString());
        for (PotionEffect effect : event.getPotionComponent().effects) {
            for (int i = 0; i < 10; i++) {
                if (effects[i] == null || effects[i] == effect) {
                    logger.info(String.valueOf(effect.duration));
                    durations[i] = effect.duration;
                    effects[i] = effect;
                    if (isScreenVisible) {
                        screen = (PotionStatusScreen) nuiManager.getScreen("potions:potionStatusScreen");
                        screen.addEffect(i, effect.effect, effect.duration);
                    }
                    delayManager.addPeriodicAction(entity, "PotionEffectUpdater" + i, 500, 500);
                    break;
                }
            }
        }
    }

    @ReceiveEvent
    public void onEffectUpdate(PeriodicActionTriggeredEvent event, EntityRef entity) {
        String actionID = event.getActionId();
        if (actionID.startsWith("PotionEffectUpdater")) {
            logger.info(actionID);
            int index = Character.getNumericValue(actionID.charAt(actionID.length() - 1));
            logger.info(String.valueOf(index));
            durations[index] -= 500;
            if (durations[index] <= 0) {
                logger.info(String.valueOf(durations[index]));
                effects[index] = null;
                durations[index] = 0;
                bubbleEffectsUp(index);
                delayManager.cancelPeriodicAction(entity, actionID);
            }
            if (isScreenVisible) {
                logger.info("Display shown");
                screen = (PotionStatusScreen) nuiManager.getScreen("potions:potionStatusScreen");
                if (effects[index] == null) {
                    screen.removeEffect(index);
                } else {
                    screen.addEffect(index, effects[index].effect, durations[index]);
                }
            }
        }
    }

    private void bubbleEffectsUp(int start) {
        for (int q = start; q < 9; q++) {
            effects[q] = effects[q + 1];
            durations[q] = durations[q + 1];
            effects[q + 1] = null;
            durations[q + 1] = 0;
        }
    }
}
