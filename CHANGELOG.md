# Changelog

## 2.1.2
* Fix dropped netherite-based items cause crash on dedicated server
* Fix disabled modules do not disable recipes after change to toml config

## 2.1.1
* Fix Coral Squid hitboxes. #351, #361
* Fix Candles not making sound when lighting
* Glow balls are now known as glowballs
* Placeable Glowstone Dust has been removed
* Update glowball texture
* **Charm's config now uses toml format.** Old json file will not be deleted, but existing config changes **will not be copied across.**

## 2.1.0
* Totem of Undying causing crash on server. Fixes #339
* VariantAnimalTextures has become VariantMobTextures. This changes the charm.json config file
* Added feature Coral Squids
* Added feature Coral Sea Lanterns
* Added Snow Golem derp face variants
* Added feature Remove Spyglass Scope (1.17 only)
* Added Kilns
* Added Refined Obsidian
* Added Smooth Glowstone
* Glow Pearls and placed glowstone can now light up any solid block face
* Removed invisible mineshaft spiders

## 2.0.4
* Added feature Placeable Glowstone Dust that lets you... place glowstone dust
* Added feature Glow Balls which are projectiles that light up the block they impact

## 2.0.3
* Darkness check for Husks and Strays, fixes them spawning in full light
* Lecterns can now be crafted from variant bookshelves. Fixes #330
* Explicit registration of Charm sounds. Relates to #333
* Changed block entity for barrel to fix a server bug. Fixes #332

## 2.0.2
* Candles are now valid decoration blocks when generating structures
* Restored vanilla chests and barrels, crafted using mixed wood
* Added hopper and minecart chest recipes with variant chests #231

## 2.0.1
* Fixed crash when trying to open lumberjack trades (dedicated server)
* Fixed missing loot tables for some custom village structures

## 2.0.0
* Fixed tidy button position on village trade screens
* Fixed broken armor invisibility when stripping in front of mobs
* Updated ladder textures
* Removed tidy button from beacon inventory
* Empty crates no longer drop themselves when in creative mode
* Added config option for chorus fruit teleport range
* Added more lumberjack and beekeeper house builds
* Added Lumberjack and Beekeeper trades
* Added Lumberjack and Beekeeper loot table for their village houses
* Beekeeper trades now use tagged flowers
* Chest texture and trim improvements
* Custom villagers now have zombie textures
* Removed "More Villager Trades" feature
* Axe now effective tool for woodcutter block
* Bump to Minecraft 1.16.3
* Removed improved lanterns due to unsolvable graphical issue
* Reduced mineshaft default ore spawn rate
* Lumberjacks log trade now only uses logs and stripped logs as requested item
* Lumberjacks now sell noteblocks and jukeboxes instead of converting logs to planks