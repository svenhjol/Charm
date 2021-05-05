package svenhjol.charm.module;

import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.base.potion.CharmPotion;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.mixin.accessor.HoglinBrainAccessor;
import svenhjol.charm.potion.HogsbaneEffect;
import svenhjol.charm.potion.HogsbanePotion;
import svenhjol.charm.potion.LongHogsbanePotion;

import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Causes all hoglins in the vicinity to run away from you.")
public class PotionOfHogsbane extends CharmModule {
    public static HogsbaneEffect HOGSBANE_EFFECT;
    public static CharmPotion HOGSPANE_POTION;
    public static CharmPotion LONG_HOGSBANE_POTION;

    @Override
    public void register() {
        HOGSBANE_EFFECT = new HogsbaneEffect(this);
        HOGSPANE_POTION = new HogsbanePotion(this);
        LONG_HOGSBANE_POTION = new LongHogsbanePotion(this);

        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (!player.world.isClient
            && player.world.getTime() % 20 == 0
            && player.hasStatusEffect(HOGSBANE_EFFECT)
        ) {
            ServerWorld world = (ServerWorld)player.world;
            BlockPos pos = player.getBlockPos();
            List<HoglinEntity> hoglins = world.getNonSpectatingEntities(HoglinEntity.class, new Box(pos).expand(12.0D));

            hoglins.forEach(hoglin -> {
                HoglinBrainAccessor.invokeAvoid(hoglin, player);
            });
        }
    }
}
