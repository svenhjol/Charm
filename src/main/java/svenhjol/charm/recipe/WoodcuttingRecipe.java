package svenhjol.charm.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import svenhjol.charm.module.Lumberjacks;

public class WoodcuttingRecipe extends CuttingRecipe {
   public WoodcuttingRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
      super(Lumberjacks.WOODCUTTING_RECIPE_TYPE, Lumberjacks.WOODCUTTING_RECIPE_SERIALIZER, id, group, input, output);
   }

   public boolean matches(Inventory inv, World world) {
      return this.input.test(inv.getStack(0));
   }

   @Environment(EnvType.CLIENT)
   public ItemStack getRecipeKindIcon() {
      return new ItemStack(Blocks.STONECUTTER);
   }


}
