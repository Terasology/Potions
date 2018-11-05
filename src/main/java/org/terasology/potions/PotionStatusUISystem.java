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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.binds.inventory.InventoryButton;
import org.terasology.logic.delay.DelayManager;
import org.terasology.logic.delay.DelayedActionTriggeredEvent;
import org.terasology.logic.delay.PeriodicActionTriggeredEvent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.potions.component.PotionEffect;
import org.terasology.potions.events.DrinkPotionEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.DefaultBinding;

import java.util.HashMap;
import java.util.Map;


@RegisterSystem(RegisterMode.CLIENT)
public class PotionStatusUISystem extends BaseComponentSystem {

    private static final String PERIODIC_ACTION_ID = "PotionStatusScreenUI";
    private static final String TARGET_SCREEN_NAME = "Core:InventoryScreen";
    private static final String WIDGET_NAME = "PotionUI";
    private static final int UPDATE_RATE = 100;
    @In
    private NUIManager nuiManager;
    @In
    private DelayManager delayManager;
    @In
    private LocalPlayer localPlayer;
    private PotionStatusWidget widget;
    private Map<String, Binding<Long>> durations = new HashMap<>();


    @Override
    public void initialise() {
    }

    /**
     * Locates the Potion Status widget on the inventory screen.
     * <p>
     * Called when the inventory button is pressed
     * Priority Low to ensure it's called /after/ the screen is opened
     *
     * @see InventoryButton
     */
    @ReceiveEvent(priority = EventPriority.PRIORITY_LOW)
    public void onInventoryButton(InventoryButton event, EntityRef entity) {
        if (nuiManager.isOpen(TARGET_SCREEN_NAME)) {
            widget = nuiManager.getScreen(TARGET_SCREEN_NAME).find(WIDGET_NAME, PotionStatusWidget.class);
        } else {
            widget = null;
        }
    }

    /**
     * Adds the effect to the screen and initiates the timer to remove it once it's done
     * <p>
     * Called when a potion is drunk
     *
     * @see DrinkPotionEvent
     */
    @ReceiveEvent
    public void onPotionDrink(DrinkPotionEvent event, EntityRef entity) {
        if (widget != null) {
            event.getPotionComponent().effects.forEach(this::addPotionEffect);
        }
    }

    /**
     * Adds a new effect to the widget.
     * Also registers for the effect to be removed when the duration gets too low.
     *
     * @param effect The effect to add
     */
    private void addPotionEffect(PotionEffect effect) {
        Binding<Long> binding = new DefaultBinding<>(effect.duration);
        durations.put(effect.effect, binding);
        widget.addOrUpdateEffect(effect.effect, binding);

        /* Register removal */
        if (delayManager.hasDelayedAction(localPlayer.getCharacterEntity(), PERIODIC_ACTION_ID + effect.effect)) {
            delayManager.cancelDelayedAction(localPlayer.getCharacterEntity(), PERIODIC_ACTION_ID + effect.effect);
        }
        delayManager.addDelayedAction(localPlayer.getCharacterEntity(), PERIODIC_ACTION_ID + effect.effect, effect.duration);

        /* Ensure update is still ticking */
        if (!delayManager.hasPeriodicAction(localPlayer.getCharacterEntity(), PERIODIC_ACTION_ID)) {
            delayManager.addPeriodicAction(localPlayer.getCharacterEntity(), PERIODIC_ACTION_ID, UPDATE_RATE, UPDATE_RATE);
        }
    }

    /**
     * Updates the duration on display in the widget
     * <p>
     * Called every {@link #UPDATE_RATE} milliseconds
     *
     * @see PeriodicActionTriggeredEvent
     */
    @ReceiveEvent
    public void onEffectUpdate(PeriodicActionTriggeredEvent event, EntityRef entity) {
        /* Check this is the right periodic trigger */
        if (!event.getActionId().equals(PERIODIC_ACTION_ID)) {
            return;
        }

        if (durations.isEmpty()) {
            delayManager.cancelPeriodicAction(entity, event.getActionId());
        } else {
            for (Binding<Long> duration : durations.values()) {
                duration.set(Math.max(0, duration.get() - UPDATE_RATE));
            }
        }
    }


    /**
     * Removes the effect from the widget once it's duration has ended.
     * <p>
     * Called when the timer is finished
     *
     * @see DelayedActionTriggeredEvent
     */
    @ReceiveEvent
    public void onDelayedActionTriggered(DelayedActionTriggeredEvent event, EntityRef entity) {
        String eventId = event.getActionId();
        if (eventId.startsWith(PERIODIC_ACTION_ID)) {
            eventId = eventId.replace(PERIODIC_ACTION_ID, "");
            if (widget != null) {
                widget.removeEffect(eventId);
            }
        }
    }
}
