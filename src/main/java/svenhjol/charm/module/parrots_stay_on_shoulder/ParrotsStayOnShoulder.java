package svenhjol.charm.module.parrots_stay_on_shoulder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.mixin.accessor.PlayerAccessor;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Parrots stay on your shoulder when jumping and falling. Crouch to make them dismount.")
public class ParrotsStayOnShoulder extends CharmCommonModule {
    private static boolean isEnabled = false;

    @Override
    public void run() {
        isEnabled = true;
        PlayerTickCallback.EVENT.register(this::tryDismountParrot);
    }

    public static boolean shouldParrotStayMounted(Level world, long shoulderTime) {
        return shoulderTime + 20L < world.getGameTime() && isEnabled;
    }

    public void tryDismountParrot(Player player) {
        if (!player.level.isClientSide
            && player.level.getGameTime() % 10 == 0
            && player.isShiftKeyDown()
        ) {
            final ServerPlayer serverPlayer = (ServerPlayer)player;
            if (!serverPlayer.getShoulderEntityLeft().isEmpty()) {
                ((PlayerAccessor)serverPlayer).invokeRespawnEntityOnShoulder(serverPlayer.getShoulderEntityLeft());
                ((PlayerAccessor)serverPlayer).invokeSetShoulderEntityLeft(new CompoundTag());
            }
            if (!serverPlayer.getShoulderEntityRight().isEmpty()) {
                ((PlayerAccessor)serverPlayer).invokeRespawnEntityOnShoulder(serverPlayer.getShoulderEntityRight());
                ((PlayerAccessor)serverPlayer).invokeSetShoulderEntityRight(new CompoundTag());
            }
        }
    }
}
