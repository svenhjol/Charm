package svenhjol.charm.module.variant_chests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import svenhjol.charm.init.CharmTags;

public class VariantChestBoatRecipe extends CustomRecipe {
    public static final String CHEST_TYPE_TAG = "chest_type";

    public VariantChestBoatRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < container.getContainerSize(); ++k) {
            ItemStack stack = container.getItem(k);
            if (!stack.isEmpty()) {
                var item = stack.getItem();
                if (item instanceof BoatItem) {
                    ++i;
                } else {
                    var block = Block.byItem(item);
                    if (!(block instanceof ChestBlock)) continue;
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
    public ItemStack assemble(CraftingContainer container) {
        var target = ItemStack.EMPTY;
        var chest = Blocks.CHEST;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            var stackInSlot = container.getItem(i);

            if (!stackInSlot.isEmpty()) {
                var item = stackInSlot.getItem();

                if (item instanceof BoatItem) {
                    target = stackInSlot;
                } else if (stackInSlot.is(CharmTags.CHESTS)) {
                    var block = Block.byItem(item);
                    if (!(block instanceof ChestBlock)) continue;
                    chest = block;
                }
            }
        }

        if (!target.isEmpty()) {
            var chestBoat = VariantChests.CHEST_BOATS.getOrDefault(target.getItem(), null);
            if (chestBoat != null) {
                if (chest instanceof VariantChestBlock variantChest) {
                    var out = new ItemStack(chestBoat);
                    var tag = new CompoundTag();
                    tag.putString(CHEST_TYPE_TAG, variantChest.getMaterialType().getSerializedName());
                    out.setTag(tag);
                    return out;
                } else if (chest instanceof ChestBlock) {
                    return new ItemStack(chestBoat);
                }
            }
        }

        // Just return some default.
        return new ItemStack(Items.OAK_CHEST_BOAT);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VariantChests.VARIANT_CHEST_BOAT_RECIPE;
    }
}
