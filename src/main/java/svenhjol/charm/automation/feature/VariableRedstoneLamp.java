package svenhjol.charm.automation.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.automation.block.BlockVariableRedstoneLight;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.registry.ProxyRegistry;

public class VariableRedstoneLamp extends Feature
{
    public BlockVariableRedstoneLight block;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockVariableRedstoneLight();

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
            " Q ", "QRQ", " Q ",
            'R', Blocks.REDSTONE_LAMP,
            'Q', Items.QUARTZ
        );
    }
}
