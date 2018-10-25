Potions
============

This module adds several potions which have a variety of effects such as healing, regen, speed increases, and status
ailment cures. This also adds a potion drinking system as well as the ability for other systems to modify the effects.
Moreover, if a potion is durable enough, an empty bottle will be returned following the potion consumption.

The following potions are present in this module:

* All Speed - Combines the effects of all the speed potions.
* Cure All - Cures all status ailments when consumed.
* Cure Poison - Cures poison status when consumed.
* Double Jump - Allows the user to double jump for 15 seconds.
* Heal - Restores 20 HP to the user.
* Health Boost - Increases the user's base max health by 50% for 30 seconds.
* Hemlock Potion - Is a worse poison potion. 
* Item Use Speed - Increases the user's item use speed by 25% for 15 seconds.
* Jump Speed - Doubles the user's jump speed for 10 seconds.
* Poison - Poisons user, dealing 3 damage per second for up to 15 seconds.
* Regen - User regenerates 3 HP per second for 10 seconds.
* Regen II - User regenerates 6 HP per second for 10 seconds ( Like regular Regen X 2 )
* Resist Physical - Temporarily increases user's physical defense by 15 for 20 seconds.
* Resist Poison - Reduces the effects of poison statuses by 2 when consumed for 15 seconds.
* Swim Speed - Doubles the user's swim speed for 10 seconds.
* Walk Speed - Doubles the user's walk speed for 10 seconds.
* Explosive - When the player uses this on an object, it triggers an explosion.
* Invincible - Makes the user invincible for 10 seconds.
* Mega Jump - Doubles all jump stats for 10 seconds
* Giga Jump - Triples all jump stats for 10 seconds
* Ninja - speeds up your running and jumping
* BlindWalk - Turns the player blind for 5 seconds along with speed decrease (Awaiting API integration for the No_Visibility effect to work)
* Battle - It increases your damage withstand limit and helps you regenrate faster.
* Super Heal - Restores a vast amount of HP
* Sacred - Regenerates 3 HP per second for 10 seconds, increases the user's base max health by 25% for 30 seconds, and also cures poison status.
* Ultra Heal - Restores a very vast amount of HP for 2 seconds.
* Divergence - Coincidentally harms and regenerates player's health, but regen's in larger amounts.
* Immobility Potion - Greatly reduces various mobility stats for 15 seconds.
* Inversion - Disorients the player by inverting the controls (i.e. turning left will turn right).
* Juggernaut - Restricts the Player's movements in exchange for damage resistance for 10 seconds.

## Contribution

Run `groovyw module recurse Potions` command while in the Terasology root folder to fetch and store the module locally. Then fork the module and add a remote reference using the command `git remote add <remote-name> <your fork>` in the `/module/Potions` directory.  

Refer to the [Developer Guide](https://github.com/Terasology/Potions/wiki/Developer-Guide) section in the wiki for further details about how to develop new potions and containers.

Send in PRs to this repository and feel free to add your name to the authors list in the [module.txt file](https://github.com/Terasology/Potions/blob/master/module.txt). Also add credits to [this README](https://github.com/Terasology/Potions/blob/master/README.md) citing sources for icons and textures.

## Credits for images:

* [Trekmarvel](https://github.com/Trekmarvel) for the base potion bottle graphics.
* Regen2PotionBottle.png edited from base by Minege
* Sacred: SacredPotionBottle.png edited from base by smsunarto 

