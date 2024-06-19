# Changelog

## 7.0.21

- Improve and clean up mixins where possible for better mod compatibility (pajicadvance)
- Aerial affinity doesn't work properly when modelled on the aqua affinity schema. Use collection "add_value"
- Animal reviving advancement now follows the animal damage immunity advancement in the tree
- Improve animal damage immunity checking. Tamed animals no longer do the "hurt" animation. Added advancement for animal damage immunity
- Chore: rename "compat mode" to "mixin disable mode"

## 7.0.22

- Fix registration of Aerial Affinity so that it doesn't always apply

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

## 7.0.24

- Fix ebony and azalea boats and chestboats not dropping the correct item when broken
- Fix missing recipe for ebony and azalea chestboats using vanilla chests
- Fix hopper and chest minecart recipes with custom chests
- Ebony boat icon no longer has normal colored oar
- Improvements to enchantment glint color balance (coranthes)
- Some cleanup of creative menu, more to follow

## 7.0.25

- Fix an issue where config was not checked properly for enabled/disabled features
- Fix advancement recipe error for hopper and chest recipes
- Fix animal armor dyeing when animal armor enchanting feature is active

## 7.0.26

- Fix unbreakable vanilla boat variants (pajicadcance)
- Updated red, lime and green enchantment glint colors (coranthes)
- Base homebrew on awkward potion so that redstone is no longer accepted as an ingredient
- Fix client desync bug with cask