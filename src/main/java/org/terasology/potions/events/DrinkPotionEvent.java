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

package org.terasology.potions.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.potions.component.PotionComponent;

/**
 * This event is sent to an entity to indicate that it'll be drinking a given potion. This will be treating the potion
 * as one entity, all effects included. Consequently, it's intended to be sent once per potion consume action.
 */
public class DrinkPotionEvent implements Event {

    /** A reference to the potion's potion component that's being consumed. */
    private PotionComponent potion;

    /** The instigator entity that is drinking this potion. */
    private EntityRef instigator;

    /** A reference to the potion item. */
    private EntityRef item;

    /**
     * Create an instance of this event with the given potion's PotionComponent.
     *
     * @param p                 The potion item's potion component. This'll be consumed by the instigator (if any).
     */
    public DrinkPotionEvent(PotionComponent p) {
        potion = p;
    }

    /**
     * Create an instance of this event with the given potion's PotionComponent as well as the instigator entity.
     *
     * @param p                 The potion item's potion component. This'll be consumed by the instigator (if any).
     * @param instigatorRef     The instigator entity who's drinking this potion.
     */
    public DrinkPotionEvent(PotionComponent p, EntityRef instigatorRef) {
        potion = p;
        instigator = instigatorRef;
    }

    /**
     * Create an instance of this event with the given potion's PotionComponent, the instigator entity, and a reference
     * to the original potion item.
     *
     * @param p                 The potion item's potion component. This'll be consumed by the instigator (if any).
     * @param instigatorRef     The instigator entity who's drinking this potion.
     * @param itemRef           A reference to the potion item entity which the instigator is consuming.
     */
    public DrinkPotionEvent(PotionComponent p, EntityRef instigatorRef, EntityRef itemRef) {
        potion = p;
        instigator = instigatorRef;
        item = itemRef;
    }

    /**
     * Get the base potion item's potion component.
     *
     * @return  The potion's PotionComponent.
     */
    public PotionComponent getPotionComponent() {
        return potion;
    }

    /**
     * Get the entity who instigated this drink event.
     *
     * @return  A reference to the instigator entity.
     */
    public EntityRef getInstigator() {
        return instigator;
    }

    /**
     * Get a reference to the original potion item entity.
     *
     * @return  A reference to the potion item.
     */
    public EntityRef getItem() { return item; }
}
