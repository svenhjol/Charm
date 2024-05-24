# Charm for Minecraft 1.21

Dev tasks:
- port advancements and:
  - advancement trigger methods and lang keys should match name of advancement
- port recipes
- port loot tables
- port tags
- ~~rename API interfaces, clean up unneeded~~
- ~~move any heavy interaction logic out of item/block and into handlers~~
- handle custom_wood and variant_wood mess in variants
- ignore unregistered data fixer errors - add feature to Silence?
- ~~all static config props to be private with accessors~~
- rename player.collection attribute to player.automatic_item_pickup
- ~~check common for matching feature in MixinConfig~~
- custom sound for coral squid bucket pickup
- custom sound when chicken sheds feather
- ~~test lumberjack and beekeeper hero gifts~~
- API provider for registering spawner drop items 
- API event for villager interaction (hook into Villager#startTrading)
- untranslated item tags warning - add tag.item.<namespace>.<path> with slashes in tag path as periods

Bugs:
- race condition in colored glints being applied, possibly MC-272311?