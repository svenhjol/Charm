package svenhjol.charm.world.feature;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.charm.crafting.feature.SuspiciousSoup;
import svenhjol.charm.enchanting.feature.CurseBreak;
import svenhjol.charm.enchanting.feature.Salvage;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.registry.VillagerRegistry;

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
        if (Charm.hasFeature(SuspiciousSoup.class)) {
            VillagerRegistry.farmer.addTrade(2, new SuspiciousSoupTrade());
            VillagerRegistry.fisherman.addTrade(2, new SuspiciousSoupTrade());
        }

        VillagerRegistry.librarian.addTrade(1, new EnchantedBookTrade());
        VillagerRegistry.cleric.addTrade(3, new CurseBreakTrade());

        VillagerRegistry.butcher.addTrade(1, new ZombieFlesh(Items.BEEF, Items.PORKCHOP, Items.RABBIT, Items.MUTTON));
        VillagerRegistry.leatherWorker.addTrade(1, new ZombieFlesh(Items.LEATHER));

        VillagerRegistry.smith.addTrade(1, new DamagedAnvilsTrade());
        VillagerRegistry.smith.addTrade(2, new LanternsTrade());
        VillagerRegistry.smith.addTrade(3, new SalvageTrade());
        VillagerRegistry.weapon.addTrade(1, new DamagedAnvilsTrade());
        VillagerRegistry.weapon.addTrade(2, new LanternsTrade());
        VillagerRegistry.weapon.addTrade(3, new SalvageTrade());
        VillagerRegistry.tool.addTrade(1, new DamagedAnvilsTrade());
        VillagerRegistry.tool.addTrade(2, new LanternsTrade());
        VillagerRegistry.tool.addTrade(3, new SalvageTrade());
        VillagerRegistry.tool.addTrade(4, new FortuneShovelTrade());
    }

    public static class SuspiciousSoupTrade implements ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int beetrootCount = random.nextInt(10) + 5;
            ItemStack soup = new ItemStack(SuspiciousSoup.suspiciousSoup, 1, random.nextInt(SuspiciousSoup.maxTypes));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.BEETROOT, beetrootCount), soup));
        }
    }

    public static class LanternsTrade implements ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int emeraldCount = random.nextInt(1) + 1;
            int lanternCount = random.nextInt(2) + 2;
            ItemStack lanterns = new ItemStack(Lantern.getDefaultLantern(), lanternCount);
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), lanterns));
        }
    }

    public static class FortuneShovelTrade implements ITradeList
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

    public static class DamagedAnvilsTrade implements ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int ironCount = random.nextInt(4) + 4;
            recipeList.add(new MerchantRecipe(new ItemStack(Blocks.ANVIL, 1, 2), new ItemStack(Items.IRON_INGOT, ironCount), new ItemStack(Blocks.ANVIL)));
        }
    }

    public static class ZombieFlesh implements ITradeList
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

    public static class SalvageTrade implements ITradeList
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

    public static class CurseBreakTrade implements ITradeList
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

    public static class EnchantedBookTrade implements ITradeList
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
