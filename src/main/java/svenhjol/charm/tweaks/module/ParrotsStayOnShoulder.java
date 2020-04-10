package svenhjol.charm.tweaks.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Parrots stay on your shoulder when jumping and falling. Crouch to make them dismount.")
public class ParrotsStayOnShoulder extends MesonModule {
    @SubscribeEvent
    public void onCrouch(PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && !event.player.world.isRemote
            && event.player.world.getGameTime() % 10 == 0
            && PlayerHelper.isCrouching(event.player)
        ) {
            final ServerPlayerEntity player = (ServerPlayerEntity)event.player;
            if (!player.getLeftShoulderEntity().isEmpty()) {
                player.spawnShoulderEntity(player.getLeftShoulderEntity());
                player.setLeftShoulderEntity(new CompoundNBT());
            }
            if (!player.getRightShoulderEntity().isEmpty()) {
                player.spawnShoulderEntity(player.getRightShoulderEntity());
                player.setRightShoulderEntity(new CompoundNBT());
            }
        }
    }
}
