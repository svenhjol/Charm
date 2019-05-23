package svenhjol.charm.tweaks.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

public class DispensersUseAnyBow extends Feature
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        RecipeHandler.removeRecipeByRegistryName(new ResourceLocation("minecraft:dispenser"));
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(Blocks.DISPENSER, 1),
            "CCC", "CBC", "CRC",
            'C', ProxyRegistry.newStack(Blocks.COBBLESTONE, 1),
            'B', ProxyRegistry.newStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE),
            'R', ProxyRegistry.newStack(Items.REDSTONE, 1)
        );
    }
}
