package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.VillagerHelper;
import svenhjol.meson.iface.Module;

import java.util.Random;

@Module(description = "Adds convenience trades to some village professions.")
public class MoreVillagerTrades extends MesonModule {
    @Override
    public void afterInit() {
        VillagerHelper.addTrade(VillagerProfession.LIBRARIAN, 3, new EmeraldsForEnchantedBookTrade());
        VillagerHelper.addTrade(VillagerProfession.BUTCHER, 3, new ItemForZombieFleshTrade(Items.PORKCHOP, 3, 6));
        VillagerHelper.addTrade(VillagerProfession.LEATHERWORKER, 3, new ItemForZombieFleshTrade(Items.LEATHER, 1, 3));
        VillagerHelper.addTrade(VillagerProfession.ARMORER, 2, new RepairedAnvilForDamagedAnvilTrade());
        VillagerHelper.addTrade(VillagerProfession.TOOLSMITH, 2, new RepairedAnvilForDamagedAnvilTrade());
        VillagerHelper.addTrade(VillagerProfession.WEAPONSMITH, 2, new RepairedAnvilForDamagedAnvilTrade());
    }

    public static class EmeraldsForEnchantedBookTrade implements TradeOffers.Factory {
        @Override
        public TradeOffer create(Entity merchant, Random rand) {
            int count = rand.nextInt(1) + 2;
            ItemStack in1 = new ItemStack(Items.ENCHANTED_BOOK); // any book
            ItemStack out = new ItemStack(Items.EMERALD, count);
            return new TradeOffer(in1, out, 10, 5, 0.2F);
        }
    }

    public static class ItemForZombieFleshTrade implements TradeOffers.Factory {
        private final Item trade;
        private final int min;
        private final int max;

        public ItemForZombieFleshTrade(Item trade, int min, int max) {
            this.trade = trade;
            this.min = min;
            this.max = max;
        }

        @Override
        public TradeOffer create(Entity merchant, Random rand) {
            int count = rand.nextInt(max - min) + min;
            ItemStack in1 = new ItemStack(Items.ROTTEN_FLESH, count * 4);
            ItemStack out = new ItemStack(trade, count);
            return new TradeOffer(in1, out, 10, 5, 0.2F);
        }
    }

    public static class RepairedAnvilForDamagedAnvilTrade implements TradeOffers.Factory {
        @Override
        public TradeOffer create(Entity merchant, Random rand) {
            ItemStack in1 = new ItemStack(rand.nextFloat() > 0.5F ? Items.DAMAGED_ANVIL : Items.CHIPPED_ANVIL);
            ItemStack in2 = new ItemStack(Items.IRON_INGOT, 4 + rand.nextInt(4));
            ItemStack out = new ItemStack(Items.ANVIL);
            return new TradeOffer(in1, in2, out, 10, 10, 0.2F);
        }
    }

}
