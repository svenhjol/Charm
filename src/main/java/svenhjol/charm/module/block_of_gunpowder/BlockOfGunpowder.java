package svenhjol.charm.module.block_of_gunpowder;

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

@CommonModule(mod = Charm.MOD_ID, description = "A storage block for gunpowder. It obeys gravity and dissolves in lava.")
public class BlockOfGunpowder extends CharmModule {
    public static GunpowderBlock GUNPOWDER_BLOCK;
    public static SoundEvent GUNPOWDER_DISSOLVE_SOUND;
    public static final ResourceLocation TRIGGER_DISSOLVED_GUNPOWDER = new ResourceLocation(Charm.MOD_ID, "dissolved_gunpowder");

    @Override
    public void register() {
        GUNPOWDER_BLOCK = new GunpowderBlock(this);
        GUNPOWDER_DISSOLVE_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "gunpowder_dissolve"));
    }

    @Override
    public void runWhenEnabled() {
        ExtraWanderingTrades.registerItem(GUNPOWDER_BLOCK, 1, 5);
    }

    public static void triggerAdvancementForNearbyPlayers(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos).forEach(player
            -> CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer)player, TRIGGER_DISSOLVED_GUNPOWDER));
    }
}
