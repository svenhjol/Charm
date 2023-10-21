package svenhjol.charm.feature.parrots_stay_on_shoulder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.event.PlayerTickEvent;

public class ParrotsStayOnShoulder extends CommonFeature {
    private static final long STAY_MOUNTED_TIME = 20L;

    @Override
    public String description() {
        return "Parrots stay on player's shoulder when jumping and falling. Crouch to make them dismount.";
    }

    @Override
    public void runWhenEnabled() {
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    public static boolean shouldParrotStayMounted(Level level, long shoulderTime) {
        return shoulderTime + STAY_MOUNTED_TIME < level.getGameTime();
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide
            && player.level().getGameTime() % 10 == 0
            && (player.isSecondaryUseActive() || player.isUnderWater())
        ) {
            var serverPlayer = (ServerPlayer)player;
            if (!serverPlayer.getShoulderEntityLeft().isEmpty()) {
                serverPlayer.respawnEntityOnShoulder(serverPlayer.getShoulderEntityLeft());
                serverPlayer.setShoulderEntityLeft(new CompoundTag());
            }
            if (!serverPlayer.getShoulderEntityRight().isEmpty()) {
                serverPlayer.respawnEntityOnShoulder(serverPlayer.getShoulderEntityRight());
                serverPlayer.setShoulderEntityRight(new CompoundTag());
            }
        }
    }
}
