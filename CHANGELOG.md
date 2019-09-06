# Changelog for Charm 1.14

## 1.3.5

The following finalized list of modules are now developed for 1.14:

### Automation
* Block of Gunpowder
* Redstone Sand
* Variable Redstone Lamp

### Brewing
* Coffee
* Decay
* Flavored Cake

### Building
* Block of Ender Pearls
* Block of Rotten Flesh
* Smooth Glowstone

### Decoration
* All the Barrels
* Bookshelf Chests
* Crates
* Gold Lanterns
* Random animal textures

### Enchanting
* Curse Break
* Homing
* Magnetic
* Salvage

### Smithing
* Decrease repair cost
* Extract enchantments
* Tallow increases durability

### Tools
* Bat in a bucket
* Compass binding
* Moonstones

### Tweaks
* Automatic recipe unlock
* Cauldron water source
* Extra records
* Husks ignore skylight
* Leather armor invisibility
* Mobs affected by beacon
* No anvil minimum XP
* Pickaxes break pistons
* Records stop background music
* Remove nitwits
* Remove potion glint
* Sponges reduce fall damage
* Stackable enchanted books
* Stackable potions
* Tamed animals take no damage
* Use totem from inventory
* Witches drop luck

### World
* Additional mobs in structures
* Endermite powder
* More village biomes
* More villager trades
* Nether gold deposits
* Structure maps

## 1.3.4

### World
* Added Structure Maps, sold by cartographers to find nearby overworld structures.


## 1.3.3

### Decoration
* Added all barrel wood types.
* Added random wolf textures.

### Enchantments
* Added Magnetic enchantment.

### Tweaks
* Tamed animals no longer take direct damage from players.


## 1.3.2

### General
* API methods to add item/block classes to crates to prevent them from being added.
* All recipes now check that feature is available before trying to add missing item recipes. Note: this is not working for loot tables, because wtf.

### Automation
* Redstone sand recipe now accepts red sand.

### Crafting
* Crates now don't allow crates or shulkerboxes to be added. Fix #146

### Tweaks
* Compasses bind to beacons, and their ability to bind to banners made false by default.
* Fixed log spam issue when transforming brewing stand stack input, added asm hook.

### World
* Nether gold deposits now fixed so they actually drop nuggets and XP.
* Totem of Returning was ported to the [Strange](https://github.com/svenhjol/Strange) mod.


## 1.3.1

### General
* Fixed: Internal version problems.
* Fixed: Possible client ticking issue.
* Changes: instead of suffixing everything with -alpha I will just flag the build.

### Crafting
* Added: Recipes for all crate wood types.

### Tweaks
* Added: Automatic recipe unlock.


## 1.3.0

This is an alpha release of Charm for 1.14.4.  Do not use it with a world you care about; everything is subject to change.

Please note that Charm 1.14.4 is a **Forge mod**.

The following list of features are mostly implemented:

### Automation

* Gunpowder Block
* Redstone Sand
* Variable Redstone Lamp

### Brewing

* Coffee
* Decay
* Flavored Cake

### Crafting

* Crates
* Ender Pearl block

### Decoration

* Gold lanterns

### Tweaks

* Extra Records
* Pickaxes break pistons
* Records stop background music
* Remove potion glint (*unique to 1.14*)
* Stackable enchanted books
* Stackable potions
* Use totem from inventory

### World

* Compass Binding
* Nether Gold Deposits
* Totem of Returning