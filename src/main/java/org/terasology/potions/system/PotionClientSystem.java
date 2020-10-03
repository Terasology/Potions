// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.system;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.potions.component.PotionComponent;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.nui.layers.ingame.inventory.GetItemTooltip;
import org.terasology.nui.widgets.TooltipLine;

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
