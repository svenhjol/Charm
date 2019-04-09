package svenhjol.charm.world.feature;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.Feature;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.charm.crafting.feature.SuspiciousSoup;
import svenhjol.charm.enchanting.feature.CurseBreak;
import svenhjol.charm.enchanting.feature.Salvage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class VillagerTrades extends Feature
{
    @Override
    public String getDescription()
    {
        return "Villagers have additional trades.  Librarians can buy back enchanted books, tool smiths repair very damaged anvils for a small iron cost, and zombie flesh is more useful.";
    }

    @SubscribeEvent
    public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event)
    {
        VillagerProfession farmerProf, librarianProf, priestProf, smithProf, butcherProf, nitwitProf;
        VillagerCareer farmer, fisherman, shepherd, fletcher, librarian, cartographer, cleric, smith, weapon, tool, butcher, leatherWorker, nitwit;

        butcherProf = event.getRegistry().getValue(new ResourceLocation("minecraft:butcher"));
        farmerProf = event.getRegistry().getValue(new ResourceLocation("minecraft:farmer"));
        priestProf = event.getRegistry().getValue(new ResourceLocation("minecraft:priest"));
        smithProf = event.getRegistry().getValue(new ResourceLocation("minecraft:smith"));
        librarianProf = event.getRegistry().getValue(new ResourceLocation("minecraft:librarian"));
//        nitwitProf = event.getRegistry().getValue(new ResourceLocation("minecraft:nitwit"));

        // assign careers from professions

        if (farmerProf != null) {
            farmer = farmerProf.getCareer(0);
            fisherman = farmerProf.getCareer(1);
//            shepherd = farmerProf.getCareer(2);
//            fletcher = farmerProf.getCareer(3);

            if (Charm.hasFeature(SuspiciousSoup.class)) {
                farmer.addTrade(2, new SuspiciousSoupTrade());
                fisherman.addTrade(2, new SuspiciousSoupTrade());
            }
        }

        if (librarianProf != null) {
            librarian = librarianProf.getCareer(0);
//            cartographer = librarianProf.getCareer(1);

            librarian.addTrade(1, new EnchantedBookTrade());
        }

        if (priestProf != null) {
            cleric = priestProf.getCareer(0);

            cleric.addTrade(3, new CurseBreakTrade());
        }

        if (butcherProf != null) {
            butcher = butcherProf.getCareer(0);
            leatherWorker = butcherProf.getCareer(1);

            butcher.addTrade(1, new ZombieFlesh(Items.BEEF, Items.PORKCHOP, Items.RABBIT, Items.MUTTON));
            leatherWorker.addTrade(1, new ZombieFlesh(Items.LEATHER));
        }

        if (smithProf != null) {
            smith = smithProf.getCareer(0);
            weapon = smithProf.getCareer(1);
            tool = smithProf.getCareer(2);

            smith.addTrade(1, new DamagedAnvilsTrade());
            smith.addTrade(2, new LanternsTrade());
            smith.addTrade(3, new SalvageTrade());
            weapon.addTrade(1, new DamagedAnvilsTrade());
            weapon.addTrade(2, new LanternsTrade());
            weapon.addTrade(3, new SalvageTrade());
            tool.addTrade(1, new DamagedAnvilsTrade());
            tool.addTrade(2, new LanternsTrade());
            tool.addTrade(3, new SalvageTrade());
            tool.addTrade(4, new FortuneShovelTrade());
        }
    }

    public static class SuspiciousSoupTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int beetrootCount = random.nextInt(10) + 5;
            ItemStack soup = new ItemStack(SuspiciousSoup.suspiciousSoup, 1, random.nextInt(SuspiciousSoup.maxTypes));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.BEETROOT, beetrootCount), soup));
        }
    }

    public static class LanternsTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = random.nextInt(1) + 1;
            int lanternCount = random.nextInt(2) + 2;
            ItemStack lanterns = new ItemStack(Lantern.ironLantern, lanternCount);
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), lanterns));
        }
    }

    public static class FortuneShovelTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = random.nextInt(5) + 12;
            ItemStack shovel = new ItemStack(Items.DIAMOND_SHOVEL);
            EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{
                put(Enchantments.FORTUNE, 3);
            }}, shovel);
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), shovel));
        }
    }

    public static class DamagedAnvilsTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int ironCount = random.nextInt(4) + 4;
            recipeList.add(new MerchantRecipe(new ItemStack(Blocks.ANVIL, 1, 2), new ItemStack(Items.IRON_INGOT, ironCount), new ItemStack(Blocks.ANVIL)));
        }
    }

    public static class ZombieFlesh implements EntityVillager.ITradeList
    {
        public final Item[] forWhat;

        public ZombieFlesh(Item... forWhat)
        {
            this.forWhat = forWhat;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int fleshCount = random.nextInt(10) + 5;
            int tradeCount = Math.min(random.nextInt(2) + 2, fleshCount);

            List<Item> items = Arrays.asList(forWhat);

            ItemStack zombieFlesh = new ItemStack(Items.ROTTEN_FLESH, fleshCount);
            ItemStack traded = new ItemStack(items.get(random.nextInt(items.size())), tradeCount);
            recipeList.add(new MerchantRecipe(zombieFlesh, traded));
        }
    }

    public static class SalvageTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = random.nextInt(3) + 2;
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(Salvage.enchantment, 1); }}, book);
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), book));
        }
    }

    public static class CurseBreakTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = random.nextInt(3) + 2;
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(CurseBreak.enchantment, 1); }}, book);
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), book));
        }
    }

    public static class EnchantedBookTrade implements EntityVillager.ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            int emeraldCount = random.nextInt(4) + 1;
            recipeList.add(new MerchantRecipe(book, new ItemStack(Items.EMERALD, emeraldCount)));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
