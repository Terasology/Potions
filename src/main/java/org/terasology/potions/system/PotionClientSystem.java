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
package org.terasology.potions.system;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.potions.component.PotionComponent;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.nui.layers.ingame.inventory.GetItemTooltip;
import org.terasology.rendering.nui.widgets.TooltipLine;

/**
 * Client system that handles how tooltips are displayed for potions.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class PotionClientSystem extends BaseComponentSystem {
    /**
     * Display the item tooltip for every potion.
     *
     * @param event             The event which contains the current list of tooltip lines.
     * @param potion            Reference to the potion entity being examined.
     * @param potionItem        A delimiter parameter used for ensuring that the examined item has a PotionComponent.
     *
     */
    @ReceiveEvent
    public void setItemTooltip(GetItemTooltip event, EntityRef potion, PotionComponent potionItem) {
        DisplayNameComponent d = potion.getComponent(DisplayNameComponent.class);

        // Only add tooltip lines if this potion item actually has a DisplayName component.
        if (d != null) {
            event.getTooltipLines().add(new TooltipLine(d.description));
        }
    }
}
