package svenhjol.charm.feature.collection;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import svenhjol.charm.foundation.helper.ItemHelper;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class CommonCallbacks {
    private static final Map<BlockPos, UUID> BREAKING = new WeakHashMap<>();

    public static void startBreaking(Player player, BlockPos pos) {
        var held = player.getMainHandItem();
        if (ItemHelper.hasEnchantment(held, Collection.enchantment.get())) {
            BREAKING.put(pos, player.getUUID());
        }
    }

    public static void stopBreaking(BlockPos pos) {
        BREAKING.remove(pos);
    }

    /**
     * @see net.minecraft.world.level.block.Block#popResource(Level, BlockPos, ItemStack)
     */
    public static boolean trySpawnToInventory(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (BREAKING.containsKey(pos)) {
                var player = level.getPlayerByUUID(BREAKING.get(pos));
                if (player != null) {
                    player.getInventory().placeItemBackInInventory(stack);
                    Collection.triggerUseCollection((ServerPlayer) player);
                    return true;
                }
            }
        }

        return false;
    }
}
