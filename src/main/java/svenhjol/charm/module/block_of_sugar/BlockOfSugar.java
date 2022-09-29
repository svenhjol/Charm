package svenhjol.charm.module.block_of_sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "A storage block for sugar. It obeys gravity and dissolves in water.")
public class BlockOfSugar extends CharmModule {
    public static SugarBlock SUGAR_BLOCK;
    public static SoundEvent SUGAR_DISSOLVE_SOUND;
    public static final ResourceLocation TRIGGER_DISSOLVED_SUGAR = new ResourceLocation(Charm.MOD_ID, "dissolved_sugar");

    @Override
    public void register() {
        SUGAR_BLOCK = new SugarBlock(this);
        SUGAR_DISSOLVE_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "sugar_dissolve"));
        ExtraWanderingTrades.registerItem(SUGAR_BLOCK, 1, 5);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos).forEach(player
            -> CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_DISSOLVED_SUGAR));
    }
}
