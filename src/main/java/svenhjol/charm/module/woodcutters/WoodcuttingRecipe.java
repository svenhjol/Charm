package svenhjol.charm.module.woodcutters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import svenhjol.charm.module.woodcutters.Woodcutters;

public class WoodcuttingRecipe extends SingleItemRecipe {
   public WoodcuttingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output) {
      super(Woodcutters.RECIPE_TYPE, Woodcutters.RECIPE_SERIALIZER, id, group, input, output);
   }

   public Ingredient getInput() {
      return this.ingredient;
   }

   public ItemStack getResultItem() {
      return this.result.copy();
   }

   public boolean matches(Container inv, Level world) {
      return this.ingredient.test(inv.getItem(0));
   }

   @Environment(EnvType.CLIENT)
   public ItemStack getRecipeKindIcon() {
      return new ItemStack(Woodcutters.WOODCUTTER);
   }
}
