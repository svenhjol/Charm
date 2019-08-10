# Changelog for Charm 1.14

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
* Totem of Returning was ported to the Strange mod.


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