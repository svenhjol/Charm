package svenhjol.charm.feature.woodcutting.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Resolve;

@SuppressWarnings("unused")
public class Recipe extends SingleItemRecipe {
   private static final Woodcutting WOODCUTTING = Resolve.feature(Woodcutting.class);
   public Item icon = Items.AIR;

   public Recipe(String group, Ingredient input, ItemStack output) {
      super(WOODCUTTING.registers.recipeType.get(), WOODCUTTING.registers.recipeSerializer.get(), group, input, output);
   }

   public Ingredient getInput() {
      return this.ingredient;
   }

   public ItemStack getResultItem() {
      return this.result.copy();
   }

   public ItemStack getRecipeKindIcon() {
      return new ItemStack(icon);
   }

   @Override
   public boolean matches(SingleRecipeInput recipeInput, Level level) {
      return this.ingredient.test(recipeInput.item());
   }
}
