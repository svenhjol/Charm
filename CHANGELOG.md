# Changelog

## 4.5.1

* Colored nether portals now show the correct color overlay when teleporting.
* Nether portal color can now be changed by right-clicking with dye as well as throwing dye.
* Remove Coral Squid debug log spam.

## 4.5.0

* Ebony trees now spawn in savanna biomes again.
* Fixed ebony leaves effective tool.
* Fixed ebony saplings not dropping.
* Moved portable crafting table button to the left of the 2x2 grid.
* Totem of Preserving now always uses grave mode.
* Added Enchantable horse armor (experimental!)

## 4.4.4

* Removed BetterEnd check.
* More totem protection when game removes or destroys blocks.
* Emergency item drop if totem block gets overwritten.

## 4.4.3

* Fixed shulkerbox sorting in dedicated server.

## 4.4.2

* Fixed grindstone ignoring enchantment.

## 4.4.1

* Compass overlay also shows biome (optional).
* Removed some debug noise.

## 4.4.0

* Removed Wandering Trader Maps.
* Added Extra Wandering Trades.
* Quick Replant now supports Collection enchantment.

## 4.3.2

* Totems of Preserving now immune to explosions.
* More debugging data added to Totems of Preserving.

## 4.3.1

* Reduced wandering trader map search distance.

## 4.3.0

* Smooth Glowstone

## 4.2.2

* Fix custom profession registration. #802 #825 #832
* Fix right click shulker item issue. #817
* Fix missing hero of the village loot for charm's villagers.

## 4.2.0

* Added ebony wood.
* Fixed issue with Totems not always spawning or being carried away by mobs.

## 4.1.3

* Fixed disabling amethyst noteblock causing IllegalArgumentException crash (#745)
* Removed player state polling due to lag issues.
* Changed Automatic Recipe Unlock to be disabled by default.
* Wandering Trader maps now only use structure tags and map names are now localised.
* Backported extra variant textures from 1.19.

## 4.1.2

* Added Bookcases.
* Fixed Mooblooms no longer stop rendering at distances further than ~16 blocks.
* Fixed bowl not returned when eating stacked soup.
* Fixed bottle not returned when drinking stacked potion/water.
* Fixed incompatibilities caused by recipe reloader mixin, relates to [this issue](https://github.com/TelepathicGrunt/RepurposedStructures-Fabric/issues/205)

## 4.1.1

* Merge fix for advancements issue #781 - [PR](https://github.com/svenhjol/Charm/pull/738)

## 4.1.0

* Added Weathering Iron. Iron blocks oxidize when any face is touching water and can be waxed and cut like copper blocks. Iron oxidizes faster when in contact with a bubble column.
* Added Colored Portals. Throw dye into a Nether portal to change the portal's color. If the dye can travel through the portal to the other side, it will change the color of the other side too.
* Fixed crash when hover sorting is disabled in config.
* Fixed coral squid erratic spawning.
* Restored original 1.17 config for Totems of Preserving. All game modes now drop totem on death (configurable).
* Removed hopper minecarts from spawning in mineshaft corridors.
* Crimson and Warped boat items are no longer fireproof.