package svenhjol.charm.module.block_of_sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.AdvancementHandler;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.block_of_sugar.SugarBlock;

@Module(mod = Charm.MOD_ID, description = "A storage block for sugar. It obeys gravity and dissolves in water.")
public class BlockOfSugar extends CharmModule {
    public static svenhjol.charm.module.block_of_sugar.SugarBlock SUGAR_BLOCK;

    public static final ResourceLocation TRIGGER_DISSOLVED_SUGAR = new ResourceLocation(Charm.MOD_ID, "dissolved_sugar");

    @Override
    public void register() {
        SUGAR_BLOCK = new SugarBlock(this);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerLevel world, BlockPos pos) {
        AdvancementHandler.getPlayersInRange(world, pos).forEach(player -> {
            CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_DISSOLVED_SUGAR);
        });
    }
}
