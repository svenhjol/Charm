package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.helper.LootHelper.*;

@SuppressWarnings("unused")
public class CharmLootTables
{
    public static ResourceLocation TREASURE_COMMON;
    public static ResourceLocation TREASURE_JUNK;
    public static ResourceLocation VILLAGE_SMITH;
    public static ResourceLocation VILLAGE_LIBRARIAN;
    public static ResourceLocation VILLAGE_PRIEST;
    public static ResourceLocation VILLAGE_CARPENTER;
    public static ResourceLocation VILLAGE_BUTCHER;
    public static ResourceLocation VILLAGE_FARMER;
    public static ResourceLocation VILLAGE_SHEPHERD;
    public static ResourceLocation VILLAGE_FISHERMAN;
    public static ResourceLocation TREASURE_COMMON_BOOKS;
    public static ResourceLocation TREASURE_COMMON_POTIONS;
    public static ResourceLocation TREASURE_UNCOMMON;
    public static ResourceLocation TREASURE_VALUABLE;
    public static ResourceLocation TREASURE_RARE;
    public static ResourceLocation TREASURE_EXPLOSIVE;
    public static ResourceLocation TREASURE_DANGEROUS;

    public static void registerLootTables()
    {
        TREASURE_COMMON = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/common"));
        TREASURE_JUNK = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/junk"));
        VILLAGE_SMITH = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/smith"));
        VILLAGE_LIBRARIAN = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/librarian"));
        VILLAGE_PRIEST = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/priest"));
        VILLAGE_CARPENTER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/carpenter"));
        VILLAGE_BUTCHER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/butcher"));
        VILLAGE_FARMER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/farmer"));
        VILLAGE_SHEPHERD = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/shepherd"));
        VILLAGE_FISHERMAN = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/fisherman"));
        TREASURE_COMMON_BOOKS = LootHelper.addLootLocation(RARITY.COMMON, TYPE.BOOK, new ResourceLocation(Charm.MOD_ID, "treasure/common_books"));
        TREASURE_COMMON_POTIONS = LootHelper.addLootLocation(RARITY.COMMON, TYPE.POTION, new ResourceLocation(Charm.MOD_ID, "treasure/common_potions"));
        TREASURE_UNCOMMON = LootHelper.addLootLocation(RARITY.UNCOMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/uncommon"));
        TREASURE_VALUABLE = LootHelper.addLootLocation(RARITY.VALUABLE, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/valuable"));
        TREASURE_RARE = LootHelper.addLootLocation(RARITY.RARE, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/rare"));
        TREASURE_EXPLOSIVE = LootHelper.addLootLocation(RARITY.SPECIAL, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/explosive"));
        TREASURE_DANGEROUS = LootHelper.addLootLocation(RARITY.SPECIAL, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/dangerous"));
    }
}