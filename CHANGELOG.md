# Changelog

### 3.1.6
* Fix bookshelves not providing enchantment power on dedicated server. svenhjol/Charm#586
* Fix typo in snowy village builds causing log spam
* Removed DirtToPath feature as it's now in vanilla
* Mineshafts no longer spawn raw ore or emerald ore
* Mineshaft central rooms may now have moss blocks and moss carpet
* Magma and lava blocks now provide cooking pot heat
* Now built against 1.17.1-pre1

### 3.1.5
* Fix coral squid hitbox, renderer and model.  svenhjol/Charm#583 svenhjol/Charm#582

### 3.1.4
* Fix casks not accepting potions
* Casks now preserve their contents when broken (configurable)
* Casks with only water now return water bottles

### 3.1.3
* Fix server-side crash with invalid client class access.  svenhjol/Charm#580
* Casks no longer accept instant potions.  svenhjol/Charm#579
* Added feature Lava Bucket Destroys Items

### 3.1.2
* Fix sign material registry issue, causing crash with expanded-storage.  svenhjol/Charm#576
* Cooking pot now updates capacity after taking bowl.  svenhjol/Charm#577
* Kiln advancement now possible by clicking output slot.  svenhjol/Charm#578
* Updated wording of cooking pot advancements

### 3.1.1
* Added missing woodcutter recipes for stripped wood types
* Temporary fix for raid horn model textures in 3rd person

### 3.1.0 (beta)
* Change villager registration.  **Existing lumberjacks and beekeepers will lose their trades.**

### 3.0.7 (beta)
* Fix accessor and loot table issues when mixins are disabled
* Fix fuel and flammability for charm wood blocks and items
* Move accessor out of the way so it doesn't conflict at AoF startup
* Cooking pot no longer consumes water if creative mode
* Configurable cask and cooking pot capacity

### 3.0.6 (beta)
* Fix issue that prevented advancements from loading conditionally
* Restored oak bookshelf, craft a vanilla bookshelf using mixed wood planks
* Azalea and ebony wood and logs can now be stripped

### 3.0.5 (beta)
* Fixed issue with extra recipe config not being applied properly
* Equipping armor from hand no longer returns it back to your hand
* Axes are now optimal tool for variant barrels, bookcases, bookshelves, chests and trapped chests

### 3.0.4 (beta)
* Fix Totem of Preserving advancement using wrong trigger
* Fix cooking pot having wrong breaking particle colors
* Mineshafts now have hanging lanterns on chains in corridors
* Pickaxes are now optimal tool for cooking pots and all metal variant things
* Axes are now optimal tool for storage crates
* Added more wood blocks as fuel source. svenhjol/Charm#549
* New candidate method for handling items mined with Collection/Acquisition.  svenhjol/Charm#566

### 3.0.3 (beta)
* Fix infinite loop when reloading textures
* Axes are the optimal tool to break casks
* Bed parts no longer rotatable by the quadrant

### 3.0.2 (beta)
* Ladders should display in decorations tab.  svenhjol/Charm#521
* Rewrite the classloader for Windows again

### 3.0.1 (beta)
* Fix classloader failure in Windows

### 3.0.0 (beta)
* Internal release