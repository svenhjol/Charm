package svenhjol.charm.module.block_of_sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "A storage block for sugar. It obeys gravity and dissolves in water.")
public class BlockOfSugar extends CharmModule {
    public static SugarBlock SUGAR_BLOCK;

    public static final ResourceLocation TRIGGER_DISSOLVED_SUGAR = new ResourceLocation(Charm.MOD_ID, "dissolved_sugar");

    @Override
    public void register() {
        SUGAR_BLOCK = new SugarBlock(this);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerLevel world, BlockPos pos) {
        PlayerHelper.getPlayersInRange(world, pos).forEach(player -> {
            CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_DISSOLVED_SUGAR);
        });
    }
}
