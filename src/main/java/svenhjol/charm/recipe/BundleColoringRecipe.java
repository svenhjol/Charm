package svenhjol.charm.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import svenhjol.charm.module.ColoredBundles;

public class BundleColoringRecipe extends SpecialCraftingRecipe {
   public BundleColoringRecipe(Identifier identifier) {
      super(identifier);
   }

   public boolean matches(CraftingInventory craftingInventory, World world) {
      int i = 0;
      int j = 0;

      for(int k = 0; k < craftingInventory.size(); ++k) {
         ItemStack itemStack = craftingInventory.getStack(k);
         if (!itemStack.isEmpty()) {
            if (itemStack.getItem() instanceof BundleItem) {
               ++i;
            } else {
               if (!(itemStack.getItem() instanceof DyeItem)) {
                  return false;
               }

               ++j;
            }

            if (j > 1 || i > 1) {
               return false;
            }
         }
      }

      return i == 1 && j == 1;
   }

   public ItemStack craft(CraftingInventory craftingInventory) {
      ItemStack itemStack = ItemStack.EMPTY;
      DyeItem dyeItem = (DyeItem)Items.WHITE_DYE;

      for(int i = 0; i < craftingInventory.size(); ++i) {
         ItemStack itemStack2 = craftingInventory.getStack(i);
         if (!itemStack2.isEmpty()) {
            Item item = itemStack2.getItem();
            if (item instanceof BundleItem) {
               itemStack = itemStack2;
            } else if (item instanceof DyeItem) {
               dyeItem = (DyeItem)item;
            }
         }
      }

      ItemStack itemStack3 = new ItemStack(ColoredBundles.COLORED_BUNDLES.get(dyeItem.getColor()));
      if (itemStack.hasTag()) {
         itemStack3.setTag(itemStack.getTag().copy());
      }

      return itemStack3;
   }

   public boolean fits(int width, int height) {
      return width * height >= 2;
   }

   public RecipeSerializer<?> getSerializer() {
      return ColoredBundles.BUNDLE_COLORING_RECIPE;
   }
}
