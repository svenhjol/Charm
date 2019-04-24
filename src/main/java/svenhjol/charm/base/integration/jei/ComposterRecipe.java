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
import svenhjol.charm.crafting.feature.Composter;

import java.util.List;

public class ComposterRecipe implements IRecipeWrapper
{
    private float chance;
    private List<ItemStack> inputs;
    private List<ItemStack> outputs;

    public ComposterRecipe(List<ItemStack> inputs, List<ItemStack> outputs, float chance)
    {
        this.chance = chance;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        int max = Composter.maxOutput;
        String numItems = max > 1 ? "up to " + max + " items" : "1 item";
        drawStringCentered(minecraft.fontRenderer, TextFormatting.DARK_GRAY + "Compost chance: " + Math.round(chance * 100) + "%", 81, 1);
        drawStringCentered(minecraft.fontRenderer, TextFormatting.DARK_GRAY + "Outputs " + numItems, 81, 68);
//        y += minecraft.fontRenderer.FONT_HEIGHT + 2;
    }

    @SideOnly(Side.CLIENT)
    private void drawStringCentered(FontRenderer fontRenderer, String text, int x, int y)
    {
        fontRenderer.drawString(text, (x - fontRenderer.getStringWidth(text) / 2), y, 0);
    }
}
