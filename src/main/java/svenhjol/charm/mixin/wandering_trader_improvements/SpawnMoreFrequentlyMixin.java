package svenhjol.charm.mixin.wandering_trader_improvements;

import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.wandering_trader_improvements.WanderingTraderImprovements;

import java.util.Random;

@Mixin(WanderingTraderManager.class)
public class SpawnMoreFrequentlyMixin {

    /**
     * Checks {@link WanderingTraderImprovements#shouldSpawnFrequently()} to
     * determine if a trader should spawn more frequently.
     *
     * If the check fails, the vanilla default (random(i) where i = 10) is used.
     */
    @Redirect(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Random;nextInt(I)I"
        )
    )
    private int hookRandomCheck(Random random, int i) {
        return WanderingTraderImprovements.shouldSpawnFrequently() ? 0 : random.nextInt(i);
    }
}
