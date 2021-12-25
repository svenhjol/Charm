package svenhjol.charm.module.parrots_stay_on_shoulder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.PlayerTickCallback;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Parrots stay on your shoulder when jumping and falling. Crouch to make them dismount.")
public class ParrotsStayOnShoulder extends CharmModule {
    @Override
    public void runWhenEnabled() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    public static boolean shouldParrotStayMounted(Level level, long shoulderTime) {
        return shoulderTime + 20L < level.getGameTime() && Charm.LOADER.isEnabled(ParrotsStayOnShoulder.class);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide
            && player.level.getGameTime() % 10 == 0
            && (player.isSecondaryUseActive() || player.isUnderWater())
        ) {
            final ServerPlayer serverPlayer = (ServerPlayer)player;
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
