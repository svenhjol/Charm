package svenhjol.charm.feature.atlases;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import svenhjol.charm.mixin.feature.atlases.MapItemSavedDataMixin;

import java.util.UUID;
import java.util.function.Predicate;

public class CommonCallbacks {
    /**
     * Callback from {@link MapItemSavedDataMixin} to check
     * if player is holding a map or a player is holding an atlas that contains a map.
     * @param inventory Inventory to check.
     * @param predicate Check?.
     * @return True if the player has a map or the player has an atlas that has a map.
     */
    public static boolean doesAtlasContainMap(Inventory inventory, Predicate<ItemStack> predicate) {
        if (inventory.contains(predicate)) {
            return true;
        }

        for (var hand : InteractionHand.values()) {
            var atlasStack = inventory.player.getItemInHand(hand);
            if (atlasStack.getItem() == Atlases.item.get()) {
                var inv = AtlasInventory.get(inventory.player.level(), atlasStack);
                if (inv.matches(predicate)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void setupAtlasUpscale(Inventory playerInventory, CartographyTableMenu container) {
        var oldSlot = container.slots.get(0);
        container.slots.set(0, new Slot(oldSlot.container, oldSlot.index, oldSlot.x, oldSlot.y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return oldSlot.mayPlace(stack) || stack.getItem() == Atlases.item.get()
                    && AtlasInventory.get(playerInventory.player.level(), stack).getMapInfos().isEmpty();
            }
        });
    }

    public static boolean makeAtlasUpscaleOutput(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, Level level,
                                                 ResultContainer craftResultInventory, CartographyTableMenu cartographyContainer) {
        if (topStack.getItem() == Atlases.item.get()) {
            ItemStack output;
            var inventory = AtlasInventory.get(level, topStack);

            if (inventory.getMapInfos().isEmpty() && bottomStack.getItem() == Items.MAP && inventory.getScale() < 4) {
                output = topStack.copy();

                AtlasData.getMutable(output)
                    .setId(UUID.randomUUID())
                    .setScale(inventory.getScale() + 1)
                    .save(output);
            } else {
                output = ItemStack.EMPTY;
            }

            if (!ItemStack.matches(output, outputStack)) {
                craftResultInventory.setItem(2, output);
                cartographyContainer.broadcastChanges();
            }

            return true;
        }

        return false;
    }
}
