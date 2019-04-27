package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockRedstoneSand;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.RecipeHandler;

public class RedstoneSand extends Feature
{
    public static float hardness;
    public static BlockRedstoneSand block;

    @Override
    public String getDescription()
    {
        return "A block that acts like sand but is powered like a block of redstone.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        hardness = 0.5f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockRedstoneSand();

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
                "RRR", "RSR", "RRR",
                'R', Items.REDSTONE,
                'S', Blocks.SAND);
    }
}
