package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.mixin.accessor.HoglinAiAccessor;
import svenhjol.charm.potion.CharmPotion;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Causes all hoglins in the vicinity to run away from you.")
public class PotionOfHogsbane extends CharmCommonModule {
    public static HogsbaneEffect HOGSBANE_EFFECT;
    public static CharmPotion HOGSPANE_POTION;
    public static CharmPotion LONG_HOGSBANE_POTION;

    public static final ResourceLocation TRIGGER_SCARED_HOGLINS = new ResourceLocation(Charm.MOD_ID, "scared_hoglins");

    @Override
    public void register() {
        HOGSBANE_EFFECT = new HogsbaneEffect(this);
        HOGSPANE_POTION = new HogsbanePotion(this);
        LONG_HOGSBANE_POTION = new LongHogsbanePotion(this);
    }

    @Override
    public void run() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide
            && player.level.getGameTime() % 20 == 0
            && player.hasEffect(HOGSBANE_EFFECT)
        ) {
            ServerLevel world = (ServerLevel)player.level;
            BlockPos pos = player.blockPosition();
            List<Hoglin> hoglins = world.getEntitiesOfClass(Hoglin.class, new AABB(pos).inflate(12.0D));

            hoglins.forEach(hoglin -> {
                HoglinAiAccessor.invokeSetAvoidTarget(hoglin, player);
            });

            if (hoglins.size() >= 1)
                PotionOfHogsbane.triggerScaredHoglins((ServerPlayer) player);
        }
    }

    public static void triggerScaredHoglins(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_SCARED_HOGLINS);
    }
}
