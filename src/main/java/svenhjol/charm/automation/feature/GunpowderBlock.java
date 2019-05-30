package svenhjol.charm.automation.feature;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.automation.block.BlockGunpowder;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

public class GunpowderBlock extends Feature
{
    public static float hardness;
    public static BlockGunpowder block;

    @Override
    public String getDescription()
    {
        return "A storage block for gunpowder.  Like Quark's sugar block, but dissolves in lava.";
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
        block = new BlockGunpowder();

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
            "GGG", "GGG", "GGG",
            'G', Items.GUNPOWDER
        );
        RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(Items.GUNPOWDER, 9),
            ProxyRegistry.newStack(block)
        );
    }
}
