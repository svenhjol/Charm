# Changelog for Charm

## 1.5.9
* Fixed issue with crates not being named after the correct wood type.
* Fixed recipes for bookshelf chests when Quark is present.
* Updated all wood type textures.
* Added lantern improvements; lanterns now obey gravity and can be waterlogged.

## 1.5.8
* Added all music discs to the minecraft music disc pool.
* Music discs now dropped by creepers when killed by skeletons, as per vanilla mechanics.
* Fixed Potion of Decay language string.
* Fixed bound compass anvil naming issue.
* Fixed bound compasses in the wrong dimension not randomly swinging.
* Removed music track that wasn't being used, saving some filesize!
* [1.15] Added back End Portal Runes.

## 1.5.7
* Fixed module enabled when sould be disabled. Fixes #247
* Added configurable parrot mimic chance. Fixes #245
* Added cauldron cleaning things.
* Added Curse of Leeching.
* Added Dimensional Compasses.
* Bound compasses no longer have an enchantment glint; dimensional compasses do.
* Updated some textures from MCVinnyq.
* JEI integration for Charm anvil recipes.
* Rotten flesh block now has configurable fertiliser chance and rate of passive growth has been nerfed.
* Bat in a bucket now only highlights living entities.

## 1.5.6
* Added parrots perching on nearby end rods.
* Crates now show contents when hovering over the item icon.
* Crates and Bookshelf chests now support Quark's item transfer, sorting and search.
* [1.15] Composter now outputs extra items correctly. Fixes #243

## 1.5.5
* Fixed music tick checking again. Fix #228
* Fixed bucket stack being consumed when catching a bat. Fix #218
* Leather armor now properly hides you from monsters. Fix #226
* [1.15] Removed rune portal frames from wither and dragon immune tags, stops console errors. Fix #227
* [1.15] Restore Quark compatibility layer.
* Improve drop chance of gold nuggets.
* Added missing redstone sand loot table.

## 1.5.4
* Added parrots staying on player shoulder when jumping.
* Added Nether pig iron ore.
* Nether pig iron nuggets are now used to repair any item instead of tallow.
* Nether gold deposits can now smelt to gold ingots.
* Structure maps are now sold by Wandering Traders instead of Cartographers.
* Website for 1.14+ and 1.12 migrated to Jekyll.

## 1.5.3
* Minor logging changes
* Some internal interface changes to prepare for 1.15+

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