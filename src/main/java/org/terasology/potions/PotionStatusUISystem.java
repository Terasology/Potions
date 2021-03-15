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

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.input.binds.general.PauseButton;
import org.terasology.module.inventory.input.InventoryButton;
import org.terasology.engine.logic.delay.DelayManager;
import org.terasology.engine.logic.delay.PeriodicActionTriggeredEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.input.ButtonState;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.events.DrinkPotionEvent;

import java.util.ArrayList;


@RegisterSystem(RegisterMode.CLIENT)
public class PotionStatusUISystem extends BaseComponentSystem {

    private static final String PERIODIC_ACTION_ID = "PotionStatusScreenUI";
    private static final String POTION_STATUS_SCREEN_NAME = "potions:potionStatusScreen";

    @In
    private NUIManager nuiManager;
    @In
    private DelayManager delayManager;
    @In
    private EntityManager entityManager;

    private final ArrayList<Float> magnitudes = new ArrayList<>();
    private final ArrayList<Long> durations = new ArrayList<>();
    private final ArrayList<String> effects = new ArrayList<>();

    private boolean isScreenVisible;
    private final int updateRate = 200;

    @Override
    public void initialise() {
    }

    @ReceiveEvent(priority = 110)
    public void inventoryToggled(InventoryButton event, EntityRef entity) {
        if (event.getState() == ButtonState.DOWN) {
            nuiManager.toggleScreen(POTION_STATUS_SCREEN_NAME);
            isScreenVisible = nuiManager.isOpen(POTION_STATUS_SCREEN_NAME);
            updateScreen();
        }
    }

    @ReceiveEvent(priority = 110)
    public void inventoryToggledOff(PauseButton event, EntityRef entity) {
        if (nuiManager.isOpen(POTION_STATUS_SCREEN_NAME)) {
            nuiManager.closeScreen(POTION_STATUS_SCREEN_NAME);
            isScreenVisible = nuiManager.isOpen(POTION_STATUS_SCREEN_NAME);
            updateScreen();
        }
    }

    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void onPotionDrink(DrinkPotionEvent event, EntityRef entity) {

        for (PotionEffect effect : event.getPotionComponent().effects) {
            int index = effects.indexOf(effect.effect);

            if (index == -1) {
                effects.add(effect.effect);
                magnitudes.add(effect.magnitude);
                durations.add(effect.duration);
                delayManager.addPeriodicAction(entity, PERIODIC_ACTION_ID + effect.effect, updateRate, updateRate);
            } else {
                effects.set(index, effect.effect);
                magnitudes.add(index, effect.magnitude);
                durations.set(index, effect.duration);
            }
        }
        updateScreen();
    }

    @ReceiveEvent
    public void onEffectUpdate(PeriodicActionTriggeredEvent event, EntityRef entity) {
        String actionID = event.getActionId();
        if (actionID.startsWith(PERIODIC_ACTION_ID)) {
            int index = effects.indexOf(actionID.substring(PERIODIC_ACTION_ID.length()));
            durations.set(index, durations.get(index) - updateRate);
            if (durations.get(index) < 0) {
                magnitudes.remove(index);
                durations.remove(index);
                effects.remove(index);
                delayManager.cancelPeriodicAction(entity, actionID);
            }
            updateScreen();
        }
    }

    private void updateScreen() {
        if (isScreenVisible) {
            PotionStatusScreen screen = (PotionStatusScreen) nuiManager.getScreen(POTION_STATUS_SCREEN_NAME);
            screen.removeAll();
            for (int i = 0; i < Math.min(15, effects.size()); i++) {
                screen.addEffect(i, effects.get(i), magnitudes.get(i), durations.get(i));
            }
        }
    }
}
