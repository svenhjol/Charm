package svenhjol.charm.foundation.helper;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class TotemHelper {
    public static boolean destroy(Player player, ItemStack totem) {
        if (player.getAbilities().instabuild) {
            return false;
        }

        totem.shrink(1);

        if (!player.level().isClientSide) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.8f, 1.0f);
        }

        return true;
    }
}
