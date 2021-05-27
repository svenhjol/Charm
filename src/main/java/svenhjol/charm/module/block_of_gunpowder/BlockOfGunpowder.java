package svenhjol.charm.module.block_of_gunpowder;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.handler.AdvancementHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, description = "A storage block for gunpowder. It obeys gravity and dissolves in lava.")
public class BlockOfGunpowder extends CharmModule {
    public static GunpowderBlock GUNPOWDER_BLOCK;

    public static final Identifier TRIGGER_DISSOLVED_GUNPOWDER = new Identifier(Charm.MOD_ID, "dissolved_gunpowder");

    @Override
    public void register() {
        GUNPOWDER_BLOCK = new GunpowderBlock(this);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerWorld world, BlockPos pos) {
        AdvancementHandler.getPlayersInRange(world, pos).forEach(player -> {
            CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayerEntity)player, TRIGGER_DISSOLVED_GUNPOWDER);
        });
    }
}
