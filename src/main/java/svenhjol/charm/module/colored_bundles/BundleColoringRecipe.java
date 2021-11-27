package svenhjol.charm.module.colored_bundles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BundleColoringRecipe extends CustomRecipe {
   public BundleColoringRecipe(ResourceLocation identifier) {
      super(identifier);
   }

   public boolean matches(CraftingContainer craftingInventory, Level level) {
      int i = 0;
      int j = 0;

      for(int k = 0; k < craftingInventory.getContainerSize(); ++k) {
         ItemStack itemStack = craftingInventory.getItem(k);
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

   @Override
   public ItemStack assemble(CraftingContainer craftingInventory) {
      ItemStack itemStack = ItemStack.EMPTY;
      DyeItem dyeItem = (DyeItem)Items.WHITE_DYE;

      for(int i = 0; i < craftingInventory.getContainerSize(); ++i) {
         ItemStack itemStack2 = craftingInventory.getItem(i);
         if (!itemStack2.isEmpty()) {
            Item item = itemStack2.getItem();
            if (item instanceof BundleItem) {
               itemStack = itemStack2;
            } else if (item instanceof DyeItem) {
               dyeItem = (DyeItem)item;
            }
         }
      }

      ItemStack itemStack3 = new ItemStack(ColoredBundles.COLORED_BUNDLES.get(dyeItem.getDyeColor()));
      if (itemStack.hasTag()) {
         itemStack3.setTag(itemStack.getTag().copy());
      }

      return itemStack3;
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   public RecipeSerializer<?> getSerializer() {
      return ColoredBundles.BUNDLE_COLORING_RECIPE;
   }
}
