// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.potions.events;

import gnu.trove.iterator.TFloatIterator;
import gnu.trove.list.TFloatList;
import gnu.trove.list.array.TFloatArrayList;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ConsumableEvent;
import org.terasology.potions.component.PotionComponent;
import org.terasology.potions.component.PotionEffect;

/**
 * This event is sent to an entity to allow modification and/or cancellation of this particular PotionEffect of a potion. The intended
 * use-case is that one instance of this event will be sent per potion effect, so individual effects of a potion can be cancelled.
 */
// TODO: Add extends AbstractValueModifiableEvent?
public class BeforeApplyPotionEffectEvent implements ConsumableEvent {

    /**
     * A reference to the potion's potion component that's going to be consumed.
     */
    private PotionComponent potion;

    /**
     * A reference to the potion effect that's going to be applied upon potion drink.
     */
    private PotionEffect pEffect;

    /**
     * The instigator entity that will drink this potion.
     */
    private EntityRef instigator;

    /**
     * A reference to the potion item.
     */
    private EntityRef item;

    /**
     * Flags whether this event been consumed or not.
     */
    private boolean consumed;

    /**
     * A list of all the multipliers for this potion effect's magnitude.
     */
    private TFloatList magnitudeMultipliers = new TFloatArrayList();

    /**
     * A list of all the multipliers for this potion effect's duration.
     */
    private TFloatList durationMultipliers = new TFloatArrayList();

    /**
     * A list of all the modifiers for this potion effect's magnitude.
     */
    private TFloatList magnitudeModifiers = new TFloatArrayList();

    /**
     * A list of all the modifiers for this potion effect's duration.
     */
    private TFloatList durationModifiers = new TFloatArrayList();

    //private TFloatList modifiers = new TFloatArrayList(); // Premodifiers specifically.

    /**
     * Create an instance of this event with the given PotionEffect of the potion that'll be applied to some entity.
     *
     * @param potionEffect The PotionEffect to be applied on the instigator.
     */
    public BeforeApplyPotionEffectEvent(PotionEffect potionEffect) {
        pEffect = potionEffect;
        instigator = EntityRef.NULL;
        item = EntityRef.NULL;
        potion = null;
    }

    /**
     * Create an instance of this event with the given PotionEffect of the potion that'll be applied to the instigator.
     *
     * @param potionEffect The PotionEffect to be applied on the instigator.
     * @param instigatorRef The entity who started to drink this potion.
     */
    public BeforeApplyPotionEffectEvent(PotionEffect potionEffect, EntityRef instigatorRef) {
        pEffect = potionEffect;
        instigator = instigatorRef;
        item = EntityRef.NULL;
        potion = null;
    }

    /**
     * Create an instance of this event with the given PotionEffect of the potion that'll be applied to the instigator. A reference to the
     * original potion is required as well.
     *
     * @param potionEffect The PotionEffect to be applied on the instigator.
     * @param instigatorRef The entity who started to drink this potion.
     * @param itemRef A reference to the potion item entity which has the potion effect.
     */
    public BeforeApplyPotionEffectEvent(PotionEffect potionEffect, EntityRef instigatorRef, EntityRef itemRef) {
        pEffect = potionEffect;
        instigator = instigatorRef;
        item = itemRef;
        potion = null;
    }

    /**
     * Create an instance of this event with the given PotionEffect of the potion that'll be applied to the instigator. A reference to the
     * original potion item is required as well, and its PotionComponent are required as well.
     *
     * @param potionEffect The PotionEffect to be applied on the instigator.
     * @param instigatorRef The entity who started to drink this potion.
     * @param itemRef A reference to the potion item entity which has the potion effect.
     * @param p The PotionComponent of the potion item.The PotionComponent of the potion item.
     */
    public BeforeApplyPotionEffectEvent(PotionEffect potionEffect, EntityRef instigatorRef, EntityRef itemRef, PotionComponent p) {
        pEffect = potionEffect;
        instigator = instigatorRef;
        item = itemRef;
        potion = p;
    }

    /**
     * Get the base potion item's potion component.
     *
     * @return The potion's PotionComponent.
     */
    public PotionComponent getBasePotion() {
        return potion;
    }

    /**
     * Get the entity who instigated this drink event.
     *
     * @return A reference to the instigator entity.
     */
    public EntityRef getInstigator() {
        return instigator;
    }

    /**
     * Get a reference to the original potion item that contains all of the PotionEffects - not just the one being checked in this event.
     *
     * @return A reference to the potion item.
     */
    public EntityRef getItem() {
        return item;
    }

    /**
     * Get a list of all the potion effect magnitude multipliers.
     *
     * @return A TFloatList containing the magnitude modifiers.
     */
    public TFloatList getMagnitudeMultipliers() {
        return magnitudeMultipliers;
    }

    /**
     * Get a list of all the potion effect duration multipliers.
     *
     * @return A TFloatList containing the duration modifiers.
     */
    public TFloatList getDurationMultipliers() {
        return durationMultipliers;
    }

    /**
     * Get a list of all the potion effect magnitude (pre)modifiers.
     *
     * @return A TFloatList containing the magnitude (pre)modifiers.
     */
    public TFloatList getMagnitudeModifiers() {
        return magnitudeModifiers;
    }

    /**
     * Get a list of all the potion effect duration (pre)modifiers.
     *
     * @return A TFloatList containing the duration (pre)modifiers.
     */
    public TFloatList getDurationModifiers() {
        return durationModifiers;
    }

    /**
     * Add a multiplier to the magnitude multipliers list.
     *
     * @param amount The value of the multiplier to add to the list.
     */
    public void multiplyMagnitude(float amount) {
        magnitudeMultipliers.add(amount);
    }

    /**
     * Add a multiplier to the duration multipliers list.
     *
     * @param amount The value of the multiplier to add to the list.
     */
    public void multiplyDuration(float amount) {
        durationMultipliers.add(amount);
    }

    /**
     * Add a (pre)modifier to the magnitude (pre)modifiers list.
     *
     * @param amount The value of the modifier to add to the list.
     */
    public void addMagnitude(float amount) {
        magnitudeModifiers.add(amount);
    }

    /**
     * Add a pre)modifier to the duration (pre)modifiers list.
     *
     * @param amount The value of the modifier to add to the list.
     */
    public void addDuration(float amount) {
        durationModifiers.add(amount);
    }

    /**
     * Add a negative (pre)modifier to the magnitude (pre)modifiers list.
     *
     * @param amount The value of the modifier to add to the list.
     */
    public void subtractMagnitude(int amount) {
        magnitudeModifiers.add(-amount);
    }

    /**
     * Add a negative (pre)modifier to the duration (pre)modifiers list.
     *
     * @param amount The value of the modifier to add to the list.
     */
    public void subtractDuration(int amount) {
        durationModifiers.add(-amount);
    }

    /**
     * Apply all of the magnitude modifiers and multipliers to get the net magnitude result value.
     *
     * @return The result of the magnitude modifier and multiplier calculations.
     */
    public float getMagnitudeResultValue() {
        // For now, add all modifiers and multiply by all multipliers. Negative modifiers cap to zero, but negative
        // multipliers remain.

        // First add the (pre)modifiers.
        float result = pEffect.magnitude;
        TFloatIterator modifierIter = magnitudeModifiers.iterator();
        while (modifierIter.hasNext()) {
            result += modifierIter.next();
        }
        result = Math.max(0, result);

        // Then, multiply the multipliers.
        TFloatIterator multiplierIter = magnitudeMultipliers.iterator();
        while (multiplierIter.hasNext()) {
            result *= multiplierIter.next();
        }

        /*
        final TFloatIterator postModifierIter = postModifiers.iterator();
        while (postModifierIter.hasNext()) {
            result += postModifierIter.next();
        }
        */
        return result;
    }

    /**
     * Apply all of the duration modifiers and multipliers to get the net duration result value.
     *
     * @return The result of the duration modifier and multiplier calculations.
     */
    public double getDurationResultValue() {
        // For now, add all modifiers and multiply by all multipliers. Negative modifiers cap to zero, but negative
        // multipliers remain.

        // First add the (pre)modifiers.
        double result = pEffect.duration;
        TFloatIterator modifierIter = durationModifiers.iterator();
        while (modifierIter.hasNext()) {
            result += modifierIter.next();
        }
        result = Math.max(0, result);

        // Then, multiply the multipliers.
        TFloatIterator multiplierIter = magnitudeMultipliers.iterator();
        while (multiplierIter.hasNext()) {
            result *= multiplierIter.next();
        }

        /*
        final TFloatIterator postModifierIter = postModifiers.iterator();
        while (postModifierIter.hasNext()) {
            result += postModifierIter.next();
        }
        */
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void consume() {
        consumed = true;
    }
}
