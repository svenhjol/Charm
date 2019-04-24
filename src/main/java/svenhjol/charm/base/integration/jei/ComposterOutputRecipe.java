package svenhjol.charm.base.integration.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ComposterOutputRecipe implements IRecipeWrapper
{
    private List<ItemStack> outputs;

    public ComposterOutputRecipe(List<ItemStack> outputs)
    {
        this.outputs = outputs;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        int y = 1;
        drawStringCentered(minecraft.fontRenderer, TextFormatting.DARK_GRAY + "Ouputs", 53, y);
//        y += minecraft.fontRenderer.FONT_HEIGHT + 2;
    }

    @SideOnly(Side.CLIENT)
    private void drawStringCentered(FontRenderer fontRenderer, String text, int x, int y)
    {
        fontRenderer.drawString(text, (x - fontRenderer.getStringWidth(text) / 2), y, 0);
    }
}
