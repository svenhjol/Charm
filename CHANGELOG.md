# Changelog

## 7.0.33

- REI integration for woodcutters and kilns
- Fix null checks in beacon effects
- Add bamboo block woodcutting recipe

## 7.0.32

- Update magenta, orange, pink and purple glint overlays (coranthes)
- Use correct custom boat ordering in creative menu tools and utilities tab
- Standardise woodcutter recipe order for all wood types
- Fix azalea and ebony signs being cursed in creative menu and woodcutter menu on server
- Additional checks for background music suppression while music discs are playing

## 7.0.31

Please note this version is for testing and might have unforeseen bugs.
Please use .29 for a more stable beta version.

- Stews and soups now stack to 16 (instead of 64). This change does not override the value in charm-common.toml
- New sound effect for feedback that pot is done cooking. Reduced "pop" sound volume when food added to pot.
- Conditional dispenser behavior to avoid conflict with vanilla and other mods
- Cooking pot dispenser support
- Removed cooking pot hopper functions, hidden inventory and ticket
- Mixed stew from a pot can be consumed more rapidly (this is subject to change)
- Mixed stew size reduced to 16 - buff stew hunger and saturation
- Fix lectern recipe using custom bookshelves

## 7.0.30

Please note this version is for testing and might have unforeseen bugs.
Please use .29 for a more stable beta version.

- Casks: now hold 16 bottles by default (configurable)
- Casks: Dispenser support. Remove hopper functionality and item buffer
- Casks: Support for instantaneous potions

## 7.0.29

- Fix ItemTidying not respecting being disabled in the config

## 7.0.28

- Fix entity rendering and mob texture register

## 7.0.27

- Incorrect logic when adding water to cask
- No longer possible to turn water into free awkward potion
- Fix client desync bugs with cask

## 7.0.26

- Fix unbreakable vanilla boat variants (pajicadcance)
- Updated red, lime and green enchantment glint colors (coranthes)
- Base homebrew on awkward potion so that redstone is no longer accepted as an ingredient
- Fix client desync bug with cask

## 7.0.25

- Fix an issue where config was not checked properly for enabled/disabled features
- Fix advancement recipe error for hopper and chest recipes
- Fix animal armor dyeing when animal armor enchanting feature is active

## 7.0.24

- Fix ebony and azalea boats and chestboats not dropping the correct item when broken
- Fix missing recipe for ebony and azalea chestboats using vanilla chests
- Fix hopper and chest minecart recipes with custom chests
- Ebony boat icon no longer has normal colored oar
- Improvements to enchantment glint color balance (coranthes)
- Some cleanup of creative menu, more to follow

## 7.0.23

- Fix returned items not going back into the correct inventory slot after adding to cooking pot
- Slightly less vibrant cooking color index. Added new particle effect when cooking is done
- Raid horn item icon minor texture change (coranthes)
- Fix filling pot with water bottle
- Pots that aren't completely full no longer have white colored water
- Now possible to throw items into the pot to cook them
- Fix block interaction when right-clicking on pot and cask with another block
- Fix incorrect recipe advancement for atlases
- Ebony leaves now drop ebony saplings
- Made ebony tree rendering a bit less derpy
- Ebony leaves no longer drop apples

## 7.0.22

- Fix registration of Aerial Affinity so that it doesn't always apply

## 7.0.21

- Improve and clean up mixins where possible for better mod compatibility (pajicadvance)
- Aerial affinity doesn't work properly when modelled on the aqua affinity schema. Use collection "add_value"
- Animal reviving advancement now follows the animal damage immunity advancement in the tree
- Improve animal damage immunity checking. Tamed animals no longer do the "hurt" animation. Added advancement for animal damage immunity
- Chore: rename "compat mode" to "mixin disable mode"