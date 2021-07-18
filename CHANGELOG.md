# Changelog

### 3.3.1
* Disable extract enchantments when grindenchantments mod is present. svenhjol/Charm#630
* Disable grindable horse armor when grindenchantments mod is present.  svenhjol/Charm#630
* Don't translate advancements on client side. svenhjol/Charm#629
* Fix incorrect dungeon build name.  svenhjol/Charm#615
* Fix for bat bucket glowing effect duration, probably
* Tweaked server side translation of charm items and advancements

### 3.3.0
* Reworked module loader. **Some minor configuration keys have changed**
* Reworked recipe and advancement filtering
* Fix Potion of Hogsbane working intermittently
* Fix advancement titles not working on server side
* Fix cooking pot renderer tick speed and item icon problems
* Simplified Music Improvements feature
* Updated colored bundle graphics
* Changing enchantment color on an anvil has now had its XP cost increased from 0 to 1 level
* Added hunger and saturation icons to mixed stew tooltips
* Added recipe for snowballs from snow blocks
* Added ModMenu and Cloth Config support
* Added BundleSorting, use scrollwheel while hovering to rotate the order of contents
* Added preliminary API for non-Charm-based mods

### 3.2.3
* Restored missing planks tag. svenhjol/Charm#606
* Translation update provided by <https://github.com/SiongSng>.  svenhjol/Charm#607
* Add compatibility tags for copper and nether nuggets. svenhjol/Charm#608
* Minor reduction of spawn chance of bookcases and storage blocks in custom structures

### 3.2.2
* Fix door check crash.  svenhjol/Charm#603

### 3.2.1
* Fix broken anvil enchantments.  svenhjol/Charm#601
* Fix neighboring door not detecting powered state.  svenhjol/Charm#602
* Netherite bars, chains and lanterns are now explosion resistant

### 3.2.0
* Fix superflat structure registration error.  svenhjol/Charm#593
* Fix incorrect totem spawn height when mounted.  svenhjol/Charm#599
* Fix missing items when spawning multiple totems.  svenhjol/Charm#600
* Added Potion of Piercing Vision
* Added Open Double Doors

### 3.1.7
* Storage crates should not accept same item with different metadata.  svenhjol/Charm#597
* Fix broken anvil repair recipes.  svenhjol/Charm#587
* Fix lanterns effective tool.  svenhjol/Charm#595
* Fix EnderBundle item creative check sided crash
* Fix EnderBundle item tooltip overlapping with normal tooltip
* Fix Storage crate hitboxes
* Slow down client storage crate effects.  svenhjol/Charm#584 
* Rework how mixed stew item tooltips render.  svenhjol/Charm#585
* Rework raid horn model.  svenhjol/Charm#572
* Convert a variant chest/bookshelf/barrel to a vanilla version on a crafting table

### 3.1.6
* Fix bookshelves not providing enchantment power on dedicated server. svenhjol/Charm#586
* Fix typo in snowy village builds causing log spam
* Removed DirtToPath feature as it's now in vanilla
* Mineshafts no longer spawn raw ore or emerald ore
* Mineshaft central rooms may now have moss blocks and moss carpet
* Magma and lava blocks now provide cooking pot heat

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