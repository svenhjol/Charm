package svenhjol.charm.tweaks.feature;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import svenhjol.meson.Feature;

public class StackablePotions extends Feature
{
    public static int stackSize;

    @Override
    public String getDescription()
    {
        return "Potions and water bottles can stack (up to 16).";
    }

    @Override
    public void setupConfig()
    {
        stackSize = 16;
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        Items.POTIONITEM.setMaxStackSize(stackSize);
        Items.SPLASH_POTION.setMaxStackSize(stackSize);
        Items.LINGERING_POTION.setMaxStackSize(stackSize);
    }
}