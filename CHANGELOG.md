# Changelog

## 6.0.9

- Fix totem not dropping if it's the only item in the inventory.
- Totem of preserving block holder now has glass particles when attacked in creative
- Fix missing azalea hanging sign drop (https://trello.com/c/viGFHWm2)
- Add sound when totem stores items on death

## 6.0.8

- Rewrite totem inventory checking API (https://trello.com/c/MEONBUqx)
- Totem of Preserving now uses the new inventory checking API (https://trello.com/c/MEONBUqx)
- Totems of Preserving now show their contents on tooltip hover
- Totem holder block now inherits from glass rather than air, because sodium
- Add missing bundle recipe
- Fix stronger anvils not working because of a typo in the feature name
- Limit proximity workstations to just crafting table for now
- Move last workstation check first in conditions checking to avoid ghost menu

## 6.0.7

- Lumberjack and beekeeper balance fixes (https://trello.com/c/9XAfucm2, https://trello.com/c/leCo6jlD)
- Port totem of preserving code
- Totem of Preserving functionality (https://trello.com/c/U4Tj4Oz4)
- Advancement and recipe handling for totem
- Add totem sound

## 6.0.6

- Lumberjack bark for logs trade now expects one emerald
- Fix lumberjack trades (https://trello.com/c/9XAfucm2)
- Fix swapped cracked bricks recipe ingredients (https://trello.com/c/EDftfJo6)
- Add Potion of Radiance (https://trello.com/c/pl2DNRO5)
- Brewing recipe registry uses suppliers

## 6.0.5

- Balance lumberjack and beekeeper recipes (https://trello.com/c/leCo6jlD)
- Changed number of ladders when woodcutting from 4 to 3 (https://trello.com/c/Fs8LdFBN)
- Additional smooth+cracked kiln recipes (https://trello.com/c/LTjfMfp5)
- Can now craft chest minecarts using variant chests (https://trello.com/c/LSxJ2rqD)
- Add shears and tallflower trades to beekeepers

## 6.0.4

- Modmenu support (https://trello.com/c/UBcTGamJ)
- Remove variant wood blocks from redstone tab (https://trello.com/c/7smSvwuv)
- Organise creative menu blocks alphabetically.
- Add fabric convention tags (https://trello.com/c/HBBzTy1R)
- Add example items for base item smithing slot (https://trello.com/c/USg5rw67)
- Add REI integration for kilns and woodcutters (https://trello.com/c/USg5rw67)
- Frame invisibility now only set/unset when an item is present (https://trello.com/c/gTqEF2lt)

## 6.0.3

- Split out cracked and smooth into variant files
- Trigger ender pearl achievement for creative players too
- Variant pistons now a fully qualified feature
- Rename custom copper piston class for consistency
- Rework wood recipe variants and templates (https://trello.com/c/oC08ISnD)
- Add recipe advancements for kiln recipes (https://trello.com/c/Q8vHCASO)
- Add missing bamboo raft woodcutting recipe (https://trello.com/c/82Naz4qa)
- Add variant ladders to woodcutter recipes
- Fix incorrect advancement ref
- Fix sign blockitem reference (https://trello.com/c/xri4aCtf)
- Recipes now sorted to fix woodcutter order (https://trello.com/c/82Naz4qa)
- Mixin PistonHeadRenderer to fix incorrect texture (https://trello.com/c/iLmVqgru)
- Don't spam menu with fixed advancements (https://trello.com/c/XNzObEY3)

## 6.0.2

- Remove "Place items in pots" feature, Mojang added it (https://www.minecraft.net/en-us/article/minecraft-snapshot-23w41a)
- Add custom advancements to chat (https://trello.com/c/O2pWCO0X)
- Add missing advancement triggers (https://trello.com/c/itw4P7Px)
- Add missing recipes (https://trello.com/c/BA4FGzxp)
- Add missing pressure plate woodcutting recipe (https://trello.com/c/smxFxiUB)
- Fix janky endermite positioning (https://trello.com/c/7WJzDJ2M)
- Fix boat dispensing (https://trello.com/c/idMDqboA)
- Fix spawn egg dispensing (https://trello.com/c/9k1jg80J)
- Fix kiln recipe category issue
- Hanging azalea sign texture fix
- Strip out variant chest boats, was too brittle
- Fix wood recipe naming

## 6.0.1

- Don't add items to pots when sneaking (https://trello.com/c/LbofOmP3)
- Add copper piston head to mineable/pickaxe tag (https://trello.com/c/UTqIPlcV)
- Default suspicious effect multiplier now 4 (from 5)
- Fix trapped chests not emitting signal (https://trello.com/c/oBjcdZ9G)
- Fix missing fuel values (https://trello.com/c/ejmq1vrK)
- Add soul speed as a valid horse armor enchantment
- Fix gunpowder advancement trigger (https://trello.com/c/00IzlJxH)
- Move colored glint smithing template advancements to decoration (https://trello.com/c/0y2o6nZc)
- Add missing overworld_natural_logs tag (https://trello.com/c/DopxW5Vo)
- Add missing enchantment_power_provider tag (https://trello.com/c/urdc4PBR)
- Fix pistons breaking pots (https://trello.com/c/clWAC2jD)