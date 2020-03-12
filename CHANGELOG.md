# Changelog for Charm

## 1.5.3
* Minor logging changes

## 1.5.2
* Rewrite loader to tackle concurrency issues and inconsistent loading
* Rewrite network handler to tackle problems when loaded with other Charm-based mods
* Internal changes to ease transition to 1.15+
* Logging fixes for better debugging and formatting log text to make it more similar to Quark
* Bump search range for structure maps from 100 to 500 blocks
* Don't allow enchantments to be extracted that are not allowed on books
* Chickens randomly drop feathers

## 1.5.1
* Fixed issue with Quark runes not working in end portal frame

## 1.5.0
* Foundation changes for Strange.
* Moonstones and custom music disc have been moved to Strange.  **May cause unregistered item warnings** if upgrading from older beta.
* More Villages feature now adds bedrock-edition village biomes.
* Lowered eruption volume and added config for it. Fix #213

## 1.4.3
* Fixed Config ignoring state of disabled modules.  This is a bit of a hack, let me know if it stops working again
* Random animal textures and Stackable items now respect config enabled/disabled
* Fixed Charm crash if another mod has some loading problem
* Fixed dispensers not firing splash potions properly if Stackable potions is enabled
* Flavored cakes don't allow you to add the same potion
* Flavored cakes don't let you add potions if they've been nommed
* When using the Homing enchant, the player will now face the ore through the world
* Added two music discs for the end: "Boss" and "The End"

## 1.4.2
* Fixed Loader ignoring state of disabled modules in config
* Fixed wolves and Quark foxhounds being invulnerable when untamed
* Added Emerald blocks open villager trades feature
* Creative mode players can now kill tamed animals

## 1.4.1
* Fixed sticky piston not being sticky
* Fixed Beacons not giving buff/debuffs
* Added Flavored Cakes for Quark potions
* Added End Portal Runes to link End Portals together
* Added ambient music improvements and merged "Records Stop Music" into it
* Added super secret Charm music disc
* Bats now take half a heart of damage when released from bucket
* Fumaroles erupt 50% less often

## 1.4.0
Initial beta release