package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockSmoothGlowstone;
import svenhjol.meson.Feature;

public class SmoothGlowstone extends Feature
{
    public static BlockSmoothGlowstone block;
    public static float hardness;

    @Override
    public String getDescription()
    {
        return "Smelt glowstone in a furnace to get smooth glowstone.";
    }

    @Override
    public void setupConfig()
    {
        hardness = 0.3f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockSmoothGlowstone();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        FurnaceRecipes instance = FurnaceRecipes.instance();

        float xp = 0.2f;
        ItemStack output = new ItemStack(block);

        instance.addSmeltingRecipeForBlock(Blocks.GLOWSTONE, output, xp);
    }
}
