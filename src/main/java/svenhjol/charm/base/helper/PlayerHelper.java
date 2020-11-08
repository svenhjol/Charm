package svenhjol.charm.base.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;

public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack) {
        if (!getInventory(player).insertStack(stack)) {
            player.dropItem(stack, true);
            return false;
        }
        return true;
    }

    public static PlayerInventory getInventory(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getInventory();
    }

    public static PlayerAbilities getAbilities(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getAbilities();
    }

    public static void openInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }
}
