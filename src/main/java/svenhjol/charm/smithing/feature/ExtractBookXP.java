package svenhjol.charm.smithing.feature;

import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

public class ExtractBookXP extends Feature
{
    public static int minXpBottles; // the minimum number of bottles returned
    public static int xpCost; // the XP cost of extraction

    @Override
    public String getDescription()
    {
        return "Combine an Enchanted Book with empty bottles on an anvil to get Bottles o' Enchanting.\n" +
                "The more levels and enchantments the book has, the more bottles are returned.";
    }

    @Override
    public void configure()
    {
        super.configure();

        minXpBottles = propInt(
                "Minimum required bottles",
                "The minimum number of glass bottles needed when converting enchantments.",
                5
        );
        xpCost = propInt(
                "XP cost",
                "Amount of XP (levels) to activate the conversion of enchantments into Bottles o' Enchanting.",
                0
        );
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (!in.isEmpty() && !combine.isEmpty()) {
            if (in.getItem() == Items.ENCHANTED_BOOK && combine.getItem() == Items.GLASS_BOTTLE && in.getCount() == 1) {
                int emptyBottles = combine.getCount();
                int xpBottles = minXpBottles;

                NBTTagList enchants = ItemEnchantedBook.getEnchantments(in);
                for (int i = 0; i < enchants.tagCount(); i++) {
                    NBTTagCompound cmp = enchants.getCompoundTagAt(i);
                    int lvl = cmp.getInteger("lvl");
                    xpBottles += (Math.pow(lvl, 2));
                }

                ItemStack out = new ItemStack(Items.EXPERIENCE_BOTTLE, Math.min(emptyBottles, xpBottles));
                event.setCost(xpCost);
                event.setMaterialCost(Math.min(emptyBottles, xpBottles));
                event.setOutput(out);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}