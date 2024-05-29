package svenhjol.charm.mixin.feature.piglin_pointing;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.piglin_pointing.PiglinPointingClient;

@Mixin(PiglinModel.class)
public abstract class PiglinModelMixin<T extends Mob> extends PlayerModel<T> {
    public PiglinModelMixin(ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
    }

    @Inject(
        method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V",
        at = @At("TAIL")
    )
    @SuppressWarnings("unchecked")
    private void hookSetupAnim(T mob, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (mob instanceof Piglin piglin) {
            Resolve.feature(PiglinPointingClient.class).handlers
                .animate((PiglinModel<Piglin>)(Object)this, piglin, f, g, h, i, j);
        }
    }
}
