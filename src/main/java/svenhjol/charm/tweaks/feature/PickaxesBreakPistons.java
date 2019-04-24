package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.Feature;

public class PickaxesBreakPistons extends Feature
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // get the piston instance and set the harvest level... ¯\_(ツ)_/¯
        BlockPistonBase basePiston = (BlockPistonBase)Block.REGISTRY.getObject(new ResourceLocation("minecraft", "piston"));
        BlockPistonBase stickyPiston = (BlockPistonBase)Block.REGISTRY.getObject(new ResourceLocation("minecraft", "sticky_piston"));

        basePiston.setHarvestLevel("pickaxe", 0);
        stickyPiston.setHarvestLevel("pickaxe", 0);
    }
}
