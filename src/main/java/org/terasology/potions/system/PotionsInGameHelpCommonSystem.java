// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions.system;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.inGameHelpAPI.event.OnAddNewCategoryEvent;

/**
 * This system is used to add the PotionsCategory help category by sending it to the appropriate handler system over in
 * the InGameHelp module.
 */
@RegisterSystem
public class PotionsInGameHelpCommonSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    /**
     * Flag for checking if the category has been sent.
     */
    boolean hasSent = false;

    @In
    private LocalPlayer localPlayer;

    /**
     * Update the system. Though in this case, only once.
     *
     * @param delta Time between this and the last update.
     */
    @Override
    public void update(float delta) {
        // Create a new instance of the AlchemyCategory and send it through an event once.
        if (!hasSent) {
            localPlayer.getClientEntity().send(new OnAddNewCategoryEvent(new PotionsCategory()));
            hasSent = true;
        }
    }
}
