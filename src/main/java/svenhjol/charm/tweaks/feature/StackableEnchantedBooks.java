package svenhjol.charm.tweaks.feature;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import svenhjol.meson.Feature;

public class StackableEnchantedBooks extends Feature
{
    public static int stackSize;

    @Override
    public String getDescription()
    {
        return "Enchanted Books can stack (up to 16).";
    }

    @Override
    public void setupConfig()
    {
        stackSize = 16;
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        Items.ENCHANTED_BOOK.setMaxStackSize(stackSize);
    }
}
