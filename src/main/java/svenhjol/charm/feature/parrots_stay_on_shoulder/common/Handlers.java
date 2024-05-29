package svenhjol.charm.feature.parrots_stay_on_shoulder.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;

public final class Handlers extends FeatureHolder<ParrotsStayOnShoulder> {
    private static final long STAY_MOUNTED_TIME = 20L;

    public Handlers(ParrotsStayOnShoulder feature) {
        super(feature);
    }

    public boolean shouldParrotStayMounted(Level level, long shoulderTime) {
        return shoulderTime + STAY_MOUNTED_TIME < level.getGameTime();
    }

    public void playerTick(Player player) {
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
