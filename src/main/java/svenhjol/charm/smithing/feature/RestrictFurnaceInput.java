package svenhjol.charm.smithing.feature;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import svenhjol.meson.Feature;

public class RestrictFurnaceInput extends Feature
{
    @Override
    public String getDescription()
    {
        return "Limit furnace input slot to allow only items that have a smelting recipe.";
    }

    public static boolean canSmelt(ItemStack stack)
    {
        return FurnaceRecipes.instance().getSmeltingResult(stack) != ItemStack.EMPTY;
    }

    @Override
    public String[] getTransformers()
    {
        return new String[] { "TileEntityFurnace", "ContainerFurnace" };
    }
}
