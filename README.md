# Charm

A Vanilla+ Minecraft mod inspired by Quark.  For Minecraft 1.12.2, Forge 14.23.5.2768+

For a full feature breakdown, please visit [https://charm.arcticfulmar.com](https://charm.arcticfulmar.com).

Please report any issues in the tracker here.

## Credits

* Includes **BetterWithMods** `canMobsSpawnHere()` method.  [Link to code](https://github.com/DaedalusGame/BetterWithMods/blob/bf630aa1fade156ce8fae0d769ad745a4161b0ba/src/main/java/betterwithmods/event/PotionEventHandler.java)
* Includes **CoFHCore** `transferEntityToWorld()` and `transferPlayerToDimension()` methods.  [Link to code](https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java)
* Includes **Shadowfacts** `transferStackInSlot()`.  [Link to code](https://github.com/shadowfacts/ShadowMC/blob/1.11/src/main/java/net/shadowfacts/shadowmc/inventory/ContainerBase.java)
* Includes **AutoRegLib** `ProxyRegistry` and `RecipeHandler`.  [Link to code](https://github.com/Vazkii/AutoRegLib/blob/master/src/main/java/vazkii/arl/util/ProxyRegistry.java)
* "Spectre" texture modelled on "Wraith" texture from **Quark**.
* Crates and other inventory code heavily inspired by [Shadowfacts' 1.12 Forge tutorials](https://shadowfacts.net/tutorials/forge-modding-112/).
* All sound samples from [https://freesound.org](https://freesound.org).

## Compatibility

* Quark r1.5-147 and r1.5-149
* Traverse 1.6.0-69.
* Dynamic Surroundings (3.5.4.3), HUDs (3.5.4), OreLib (3.5.2.2).
* JEI (4.14.3.239).
* HYWLA (1.8.26).
* Temporarily disabled: Inventory Tweaks containers support, tested with 1.64+dev.146.
* Mystcraft support is limited. Village biomes in ages might not detect village seed or biome properly.