package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.helper.LootHelper.*;

@SuppressWarnings("unused")
public class CharmLootTables
{
    public static final ResourceLocation TREASURE_COMMON = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/common"));
    public static final ResourceLocation TREASURE_JUNK = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/junk"));
    public static final ResourceLocation VILLAGE_SMITH = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/smith"));
    public static final ResourceLocation VILLAGE_LIBRARIAN = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/librarian"));
    public static final ResourceLocation VILLAGE_PRIEST = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/priest"));
    public static final ResourceLocation VILLAGE_CARPENTER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/carpenter"));
    public static final ResourceLocation VILLAGE_BUTCHER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/butcher"));
    public static final ResourceLocation VILLAGE_FARMER = LootHelper.addLootLocation(RARITY.COMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "village/farmer"));
    public static final ResourceLocation TREASURE_COMMON_BOOKS = LootHelper.addLootLocation(RARITY.COMMON, TYPE.BOOK, new ResourceLocation(Charm.MOD_ID, "treasure/common_books"));
    public static final ResourceLocation TREASURE_COMMON_POTIONS = LootHelper.addLootLocation(RARITY.COMMON, TYPE.POTION, new ResourceLocation(Charm.MOD_ID, "treasure/common_potions"));
    public static final ResourceLocation TREASURE_UNCOMMON = LootHelper.addLootLocation(RARITY.UNCOMMON, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/uncommon"));
    public static final ResourceLocation TREASURE_VALUABLE = LootHelper.addLootLocation(RARITY.VALUABLE, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/valuable_tools"));
    public static final ResourceLocation TREASURE_RARE = LootHelper.addLootLocation(RARITY.RARE, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/rare"));
    public static final ResourceLocation TREASURE_EXPLOSIVE = LootHelper.addLootLocation(RARITY.SPECIAL, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/explosive"));
    public static final ResourceLocation TREASURE_DANGEROUS = LootHelper.addLootLocation(RARITY.SPECIAL, TYPE.MISC, new ResourceLocation(Charm.MOD_ID, "treasure/dangerous"));
}