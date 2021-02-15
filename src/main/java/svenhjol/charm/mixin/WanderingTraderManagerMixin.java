package svenhjol.charm.mixin;

import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.WanderingTraderImprovements;

import java.util.Random;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    @Redirect(
        method = "method_18018",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Random;nextInt(I)I"
        )
    )
    private int hookRandomCheck(Random random, int i) {
        return WanderingTraderImprovements.shouldSpawnFrequently() ? 0 : random.nextInt(i);
    }
}
