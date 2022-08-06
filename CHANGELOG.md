# Changelog

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