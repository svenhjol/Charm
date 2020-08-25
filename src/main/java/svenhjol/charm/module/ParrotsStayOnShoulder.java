package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Parrots stay on your shoulder when jumping and falling. Crouch to make them dismount.")
public class ParrotsStayOnShoulder extends MesonModule {
    @Override
    public void init() {
        if (!enabled)
            return;

        PlayerTickCallback.EVENT.register(this::dismountParrot);
    }

    public static boolean shouldParrotStayMounted(World world, long shoulderTime) {
        return shoulderTime + 20L < world.getTime() && Meson.enabled("charm:parrots_stay_on_shoulder");
    }

    public void dismountParrot(PlayerEntity player) {
        if (!player.world.isClient
            && player.world.getTime() % 10 == 0
            && player.isSneaking()
        ) {
            final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            if (!serverPlayer.getShoulderEntityLeft().isEmpty()) {
                ((PlayerEntityAccessor)serverPlayer).callDropShoulderEntity(serverPlayer.getShoulderEntityLeft());
                ((PlayerEntityAccessor)serverPlayer).callSetShoulderEntityLeft(new CompoundTag());
            }
            if (!serverPlayer.getShoulderEntityRight().isEmpty()) {
                ((PlayerEntityAccessor)serverPlayer).callDropShoulderEntity(serverPlayer.getShoulderEntityRight());
                ((PlayerEntityAccessor)serverPlayer).callSetShoulderEntityRight(new CompoundTag());
            }
        }
    }
}
