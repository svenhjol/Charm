package svenhjol.charm.base.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
            player.dropItem(stack, true);
            return false;
        }
        return true;
    }

    public static void teleport(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            player.requestTeleport(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
            world.sendEntityStatus(player, (byte) 46);
            world.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.5F, 1.25F);
        }
    }

    public static void openInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }
}
