# Changelog

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
* Fix lumberjack registration causing tag failures
* Fix right click shulker item issue. #817
* Fix missing hero of the village loot for charm's villagers.
* Fix accessor in fabric API 0.60.0. #838
* Beehives now get their maxtickets increased for Beekeepers via mixin.

## 4.2.1

* Fix variant chest boats losing contents on relog. #800
* Fix vanilla chest boat items not showing correct chest colors.

## 4.2.0

* Added ebony wood.
* Added No Chat Verified Nag which disables the popup message when connecting to a server with `enforce-secure-profile` turned off.
* Fixed issue with Totems not always spawning or being carried away by mobs.

## 4.1.3

* Fixed disabling amethyst noteblock causing IllegalArgumentException crash (#745)
* Removed player state polling due to lag issues.
* Changed Automatic Recipe Unlock to be disabled by default.
* Wandering Trader maps now only use structure tags and map names are now localised.

## 4.1.2

* Added Bookcases.
* Fixed Mooblooms no longer stop rendering at distances further than ~16 blocks.
* Fixed bowl not returned when eating stacked soup.
* Fixed bottle not returned when drinking stacked potion/water.
* Fixed incompatibilities caused by recipe reloader mixin, relates to [this issue](https://github.com/TelepathicGrunt/RepurposedStructures-Fabric/issues/205)
* 1.19: Variant chests can be added to boats.
* 1.19: New variant textures for Turtles, Dolphins and Wandering Traders.
* 1.19: Crimson and Warped boats have been removed.


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