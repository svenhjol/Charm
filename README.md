# Charm for Minecraft 1.18.1

## Version 4.x
Charm is a vanilla-plus mod for Minecraft, inspired by the Quark mod.

It requires Fabric API, Minecraft 1.18.1 and Java 17.

Charm is currently in internal testing.  A beta release is due on or before **17/12/21**.

### Unchanged features
* `Aerial affinity enchantment`
* `Auto restock`
* `Azalea wood`
* `Beacons heal mobs`
* `Block of ender pearls`
* `Block of gunpowder`
* `Block of sugar`
* `Cave spiders drop cobwebs`
* `Chickens drop feathers`
* `Clear item framrs`
* `Collection enchantment`
* `Colored bundles`
* `Coral sea lanterns`
* `Coral squids`
* `Editable signs`
* `Endermite powder`
* `Extra boats`
* `Gentle potion particles`
* `Inventory tidying`
* `Kilns`
* `Lava bucket destroys items`
* `Mineshaft improvements`
* `Mooblooms`
* `More portal frames`
* `No potion glint`
* `No spyglass scope`
* `Parrots stay on shoulder`
* `Path to dirt`
* `Player pressure plates`
* `Portable crafting`
* `Redstone lanterns`
* `Redstone sand`
* `Stackable enchanted books`
* `Stackable potions`
* `Variant barrels`
* `Variant bookshelves`
* `Variant ladders`
* `Variant mob textures`
* `Villagers follow emerald blocks`
* `Wandering trader maps`
* `Witches drop luck`
* `Woodcutters`


### Added/changed features
* `Anvil improvements` has been split into the following features:
  * `Allow too expensive`
  * `Show repair cost`
  * `Stronger anvils`
* `Amethyst note block` has been added.
* `Atlases`:
  * A keybind has been added to quickly toggle between mainhand and atlas.
* `Bat buckets` now give the player the *echolocation* effect.
  * The echolocation effect can be applied to the player via commands to cause all nearby entities to receive the glowing effect.
* `Beekeepers`:
  * No longer have a dedicated village house.
  * The village house has been moved to the *Strange* mod.
* `Bumblezone` is now known as `Bumblezone integration`.
* `Bundle sorting` has been changed to `Hover sorting` and is also responsible for sorting shulker boxes.
* `Extra trades` has been added.
* `Extract enchantments`:
  * Extraction and treasure enchantments are more expensive (configurable).
* `Feather falling crops` is now known as `No crop trampling`.
  * Feather falling boots is now a configurable option.
* `Goats drop mutton` has been added.
* `Hoe harvesting` is now known as `Quick replant`.
  * It is now possible to add custom crop blockstates to the config.
* `Husk improvements` has been split into the following features:
  * `Husks drop sand`
  * `Husks spawn underground`
* `Lower noteblock pitch` has been added.
* `Lumberjacks`:
  * No longer have a dedicated village house.
  * The village house has been moved to the *Strange* mod.
* `Map tooltips` is now known as `Map tooltip`.
  * Now uses decoration blocks from dedicated loot table files.
* `Music improvements` is now known as `Discs stop background music`.
  * The creative music tracks have moved to the *Charmonium* mod.
* `More village biomes`:
  * Now adds villages to the new meadows biome.
* `No cured villager discount` has been added and is **disabled by default**.
* `No treasure enchantment trading` has been added and is **disabled by default**.
* `Open double doors` is now known as `Open both doors`.
* `Raid horns`:
  * New sound for when calling off a raid.
  * Sounds added for if a raid horn fails to activate.
* `Remove nitwits` is now known as `No nitwits` and is **disabled by default**.
* `Respawn anchor in the End` has been added and is **disabled by default**.
* `Shulker box drag drop` has been added.
* `Shulker box tooltips` is now known as `Shulker box tooltip`.
  * The tooltip uses the vanilla bundle tooltip hover gui.
* `Snow storms` has been reworked and is now known as `Snow accumulation`.
  * The freezing penalty has been removed.
  * The heavier snow texture has been removed.
* `Stackable stews` now defaults to a stacksize of 16.
* `Stray improvements` has been split into the following features:
  * `Strays drop blue ice`
  * `Strays spawn underground`
* `Tamed animals no damage` is now known as `No pet damage`.
* `Totem of preserving` has been reworked:
  * Peaceful and Easy mode always drop a totem on death.
  * Normal and Hard mode require you to have a totem in your inventory.
  * Totems can be dropped by witches or found in outposts/mansions.
* `Use totem from inventory` is now known as `Totem works from inventory`.
  * It now affects the totem of preserving as well as the totem of undying.
* `Variant bars` textures now match the gold, copper and netherite blocks palette more closely.
* `Variant chains` now only applies to gold.
* `Variant lanterns` now only applies to gold.


### Removed/moved features
* `Armor invisibility` has been removed.
* `Biome dungeons` has been removed.
* `Bookcases` has been removed.
* `Casks` has moved to the *Strange* mod.
* `Campfires no damage` has been removed.
* `Colored glints` has moved to the *Strange* mod.
* `Cooking pots` has moved to the *Strange* mod.
* `Copper rails` has been removed.
* `Decrease repair cost` has been removed.
* `Ebony wood` has moved to the *Strange* mod.
* `Ender bundles` has moved to the *Strange* mod.
* `Entity spawners` has moved to the *Strange* mod.
* `Extra nuggets` has been removed.
* `Glowballs` has moved to the *Strange* mod.
* `Piglins follow gold blocks`
* `Potion of Hogsbane` has moved to the *Strange* mod.
* `Potion of Piercing Vision` has moved to the *Strange* mod.
* `Potion of Spelunking` has moved to the *Strange* mod.
* `Quadrants` has moved to the *Strange* mod.
* `Storage crates` has been removed.
* `Storage labels` has been removed.


### Mixins and accessors
* All accessor mixins have been removed.  Accesswideners are now used instead.
* If a feature is disabled in the config, all of its associated mixins will **not be added**.  This is intended to improve compatibility with other mods that might patch the same code.
* Mixins use `inject`s as often as possible to avoid conflicts.
* To blacklist any mixin, create a file in the config called `charm-mixin-blacklist.txt` and add each mixin to disable on its own line in the following format: `module_name.MixinClassName`
  * example: `no_treasure_enchantment_trading.CheckIfTradeableMixin`
* The `helper.LoadCustomModelPartsMixin` cannot be blacklisted as Charm requires it to render custom models and will otherwise cause a client-side crash.
* `helper`, `core` and `event` mixins can be blacklisted but this is not recommended as doing so will cause inconsistent behavior in Charm.


### Support
**Charm 4.x for Minecraft 1.18 is now the main version of Charm.**

Charm for Minecraft 1.17 (and older versions) will no longer receive official support.
Issues opened for these older versions will not be monitored or prioritised.

A Forge port of Charm will be considered when Forge 1.18 is stable.

If you would like to support older versions or offer assistance with beta testing you can contact me via DM (I lurk on The Fabric Project discord as `svenhjol`).