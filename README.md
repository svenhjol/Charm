# Charm for Minecraft 1.18.1

## Version 4.x
Charm is currently in testing.
A number of features have been changed, removed or moved to other mods.
If the feature is not listed here then its functionality remains the same from Charm 3.x.

* `Anvil improvements` has been split into the following features:
  * `Allow too expensive`
  * `Stronger anvils`
* `Amethyst note block` has been added.
* `Armor invisibility` has been removed.
* `Bat buckets` now give the player the *echolocation* effect.
  * The echolocation effect can be applied to the player via commands to cause all nearby entities to receive the glowing effect.
* `Beekeepers` no longer have a dedicated village house.
  * The village house has been moved to the *Strange* mod.
* `Biome dungeons` has been removed.
* `Bookcases` has been removed.
* `Bundle sorting` has been changed to `Hover sorting` and is also responsible for sorting shulker boxes.
* `Campfires no damage` has been removed.
* `Casks` has been reworked and moved to the *Strange* mod.
* `Colored glints` has been moved to the *Strange* mod.
* `Cooking pots` has been reworked and moved to the *Strange* mod.
* `Copper rails` has been removed.
* `Decrease repair cost` has been removed.
* `Ebony wood` has been moved to the *Strange* mod.
* `Ender bundles` has been moved to the *Strange* mod.
* `Entity spawners` has been moved to the *Strange* mod.
* `Extract enchantments` is now more expensive (configurable).
* `Extra trades` has been added.
* `Extra nuggets` has been removed.
* `Feather falling crops` is now known as `No crop trampling`.
  * Feather falling boots is now a configurable option.
* `Glowballs` has been moved to the *Strange* mod.
* `Goats drop mutton` has been added.
* `Hoe harvesting` is now known as `Quick replant`.
  * It is now possible to add custom crop blockstates to the config.
* `Husk improvements` has been split into the following features:
  * `Husks drop sand`
  * `Husks spawn underground`
* `Lower noteblock pitch` has been added.
* `Lumberjacks` no longer have a dedicated village house.
  * The village house has been moved to the *Strange* mod.
* `Mineshaft improvements` now pulls decoration blocks from dedicated loot table files.
* `Music improvements` is now known as `Discs stop background music`.
  * The creative music tracks have moved to the *Charmonium* mod.
* `More village biomes` now adds villages to the new meadows biome.
* `No treasure enchantment trading` has been added and is **disabled by default**.
* `Open double doors` is now known as `Open both doors`.
* `Piglins follow gold blocks` has been removed.
* `Potion of Hogsbane` has been moved to the *Strange* mod.
* `Potion of Piercing Vision` has been moved to the *Strange* mod.
* `Potion of Spelunking` has been moved to the *Strange* mod.
* `Quadrants` has been moved to the *Strange* mod.
* `Remove nitwits` is now known as `No nitwits` and is **disabled by default**.
* `Respawn anchor in the End` has been added and is **disabled by default**.
* `Scale cured villager discount` has been added and is **disabled by default**.
* `Shulker box drag drop` has been added.
* `Shulker box tooltips` is now known as `Shulker box tooltip`.
  * The tooltip uses the vanilla bundle tooltip hover gui.
* `Snow storms` has been reworked and is now known as `Snow accumulation`.
  * The freezing penalty has been removed.
  * The heavier snow texture has been removed.
* `Stackable stews` now defaults to a stacksize of 16.
* `Storage crates` has been removed.
* `Storage labels` has been removed.
* `Stray improvements` has been split into the following features:
  * `Strays drop blue ice`
  * `Strays spawn underground`
* `Tamed animals no damage` is now known as `No pet damage`.
* `Totem of preserving` has been reworked.
  * Peaceful and Easy mode always drop a totem on death.
  * Normal and Hard mode require you to have a totem in your inventory.
  * Totems can be dropped by witches or found in outposts/mansions.
* `Use totem from inventory` is now known as `Totem works from inventory`.
  * It now affects the totem of preserving as well as the totem of undying.
* `Variant bars` textures now match the gold, copper and netherite blocks palette more closely.
* `Variant chains` now only applies to gold.
* `Variant lanterns` now only applies to gold.

## Mixins and accessors
* All accessor mixins have been removed.  Accesswideners are now used instead.
* If a feature is disabled in the config, all of its associated mixins will **not be added**.  This is intended to improve compatibility with other mods that might patch the same code.
* Mixins use `inject`s as often as possible to avoid conflicts.
* To blacklist any mixin, create a file in the config called `charm-mixin-blacklist.txt` and add each mixin to disable on its own line in the following format: `module_name.MixinClassName`
  * example: `no_treasure_enchantment_trading.CheckIfTradeableMixin`
* The `helper.LoadCustomModelPartsMixin` cannot be blacklisted as Charm requires it to render custom models and will otherwise cause a client-side crash.
* `helper`, `core` and `event` mixins can be blacklisted but this is not recommended as doing so will cause inconsistent behavior in Charm.
