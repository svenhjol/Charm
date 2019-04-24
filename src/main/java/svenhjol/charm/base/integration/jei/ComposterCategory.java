package svenhjol.charm.base.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;

import java.util.List;

public class ComposterCategory implements IRecipeCategory
{
    private final IGuiHelper guiHelper;
    public static final String UID = "CHARM_COMPOSTER_CATEGORY";

    public ComposterCategory(IGuiHelper guiHelper)
    {
        this.guiHelper = guiHelper;
    }

    @Override
    public String getUid()
    {
        return UID;
    }

    @Override
    public String getTitle()
    {
        return "Composter"; /* @todo Localize */
    }

    @Override
    public String getModName()
    {
        return Charm.MOD_NAME;
    }

    @Override
    public IDrawable getBackground()
    {
        return guiHelper.createDrawable(new ResourceLocation(Charm.MOD_ID, "textures/gui/generic27.png"), 7, 5, 163, 96);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

//        guiItemStacks.init(0, false, 118, 10);

        // position input items
        int y = 12;
        int i = 0;

        int j = 0;
        int k = 0;
        for (List<ItemStack> stacks : ingredients.getInputs(ItemStack.class)) {
            for (ItemStack stack : stacks) {
                guiItemStacks.init(i, true, j * 18, y + (k * 18));
                guiItemStacks.set(i, stack);
                j = ++i % 9;
                k += j == 0 ? 1 : 0;
            }
        }

        // position output items
        y += 66;
        i = 27;
        j = 0;
        k = 0;
        for (List<ItemStack> stacks : ingredients.getOutputs(ItemStack.class)) {
            for (ItemStack stack : stacks) {
                guiItemStacks.init(i, true, j * 18, y + (k * 18));
                guiItemStacks.set(i, stack);
                j = ++i % 9;
                k += j == 0 ? 1 : 0;
            }
        }
//        guiItemStacks.init(i, false, 118, 10);
//        guiItemStacks.set(i, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
