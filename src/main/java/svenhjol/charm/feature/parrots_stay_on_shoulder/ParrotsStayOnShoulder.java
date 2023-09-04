package svenhjol.charm.feature.parrots_stay_on_shoulder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.PlayerAccessor;
import svenhjol.charmony.api.event.PlayerTickEvent;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Parrots stay on player's shoulder when jumping and falling. Crouch to make them dismount.")
public class ParrotsStayOnShoulder extends CharmFeature {
    private static final long STAY_MOUNTED_TIME = 20L;

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
