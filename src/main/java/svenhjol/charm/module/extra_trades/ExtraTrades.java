package svenhjol.charm.module.extra_trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Adds more villager trades.")
public class ExtraTrades extends CharmModule {
    @Config(name = "Enchanted books", description = "If true, librarians will buy any enchanted book in return for emeralds.")
    public static boolean enchantedBooks = true;

    @Config(name = "Repaired anvils", description = "If true, armorers, weaponsmiths and toolsmiths will buy chipped or damaged anvils along with iron ingots in return for repaired anvils.")
    public static boolean repairedAnvils = true;

    @Config(name = "Leather for rotten flesh", description = "If true, leatherworkers will sell leather in return for rotten flesh.")
    public static boolean leatherForRottenFlesh = true;

    @Config(name = "Beef for rotten flesh", description = "If true, butchers will sell beef in return for rotten flesh.")
    public static boolean beefForRottenFlesh = true;

    @Config(name = "Bundles", description = "If true, leatherworkers will sell bundles in return for emeralds.")
    public static boolean bundles = true;

    /**
     * MerchantOffer:
     * - ItemStack in1
     * - (ItemStack in2)
     * - ItemStack out
     * - int maxUses
     * - int villagerXp
     * - float priceMultiplier
     */

    @Override
    public void runWhenEnabled() {
        if (enchantedBooks) {
            int tier = 2;
            VillagerHelper.addTrade(VillagerProfession.LIBRARIAN, tier, (entity, random) -> {
                Item in = Items.ENCHANTED_BOOK;
                Item out = Items.EMERALD;
                return new MerchantOffer(new ItemStack(in), new ItemStack(out, 5), 16, 5, 0.05F);
            });
        }

        if (repairedAnvils) {
            int tier = 2;
            VillagerHelper.addTrade(VillagerProfession.ARMORER, tier, this::anvilRepairTrade);
            VillagerHelper.addTrade(VillagerProfession.WEAPONSMITH, tier, this::anvilRepairTrade);
            VillagerHelper.addTrade(VillagerProfession.TOOLSMITH, tier, this::anvilRepairTrade);
        }

        if (leatherForRottenFlesh) {
            int tier = 3;
            VillagerHelper.addTrade(VillagerProfession.LEATHERWORKER, tier, (entity, random) -> {
                Item in = Items.ROTTEN_FLESH;
                Item out = Items.LEATHER;
                int cost = random.nextInt(5) + 10;
                return new MerchantOffer(new ItemStack(in, cost), new ItemStack(out), 8, 10, 0.05F);
            });
        }

        if (beefForRottenFlesh) {
            int tier = 3;
            VillagerHelper.addTrade(VillagerProfession.BUTCHER, tier, (entity, random) -> {
                Item in = Items.ROTTEN_FLESH;
                Item out = Items.BEEF;
                int cost = random.nextInt(5) + 8;
                return new MerchantOffer(new ItemStack(in, cost), new ItemStack(out), 8, 10, 0.05F);
            });
        }

        if (bundles) {
            int tier = 5;
            VillagerHelper.addTrade(VillagerProfession.LEATHERWORKER, tier, (entity, random) -> {
                Item in = Items.EMERALD;
                Item out = Items.BUNDLE;
                int cost = random.nextInt(10) + 12;
                return new MerchantOffer(new ItemStack(in, cost), new ItemStack(out), 1, 30, 0.05F);
            });
        }
    }

    private MerchantOffer anvilRepairTrade(Entity entity, RandomSource random) {
        Item in1 = random.nextBoolean() ? Items.DAMAGED_ANVIL : Items.CHIPPED_ANVIL;
        Item in2 = Items.IRON_INGOT;
        Item out = Items.ANVIL;
        int cost = random.nextInt(4) + 5;
        return new MerchantOffer(new ItemStack(in1), new ItemStack(in2, cost), new ItemStack(out), 1, 5, 0.05F);
    }
}
