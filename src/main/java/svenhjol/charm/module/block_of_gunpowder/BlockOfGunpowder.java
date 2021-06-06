package svenhjol.charm.module.block_of_gunpowder;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.handler.AdvancementHandler;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.CharmModule;

@Module(mod = Charm.MOD_ID, description = "A storage block for gunpowder. It obeys gravity and dissolves in lava.")
public class BlockOfGunpowder extends CharmModule {
    public static GunpowderBlock GUNPOWDER_BLOCK;

    public static final ResourceLocation TRIGGER_DISSOLVED_GUNPOWDER = new ResourceLocation(Charm.MOD_ID, "dissolved_gunpowder");

    @Override
    public void register() {
        GUNPOWDER_BLOCK = new GunpowderBlock(this);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerLevel world, BlockPos pos) {
        AdvancementHandler.getPlayersInRange(world, pos).forEach(player -> {
            CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_DISSOLVED_GUNPOWDER);
        });
    }
}
