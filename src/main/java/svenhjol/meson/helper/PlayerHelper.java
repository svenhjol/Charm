package svenhjol.meson.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack) {
        if (!player.inventory.insertStack(stack)) {
            player.dropItem(stack, false);
            return false;
        }
        return true;
    }

    public static void openInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }
}
