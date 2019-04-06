package svenhjol.charm.misc.feature;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import svenhjol.meson.Feature;

public class StackableMilkBuckets extends Feature
{
    public static int stackSize;

    @Override
    public String getDescription()
    {
        return "Milk buckets can stack (up to 16).";
    }

    @Override
    public void setupConfig()
    {
        stackSize = 16;
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        Items.MILK_BUCKET.setMaxStackSize(stackSize);
    }
}