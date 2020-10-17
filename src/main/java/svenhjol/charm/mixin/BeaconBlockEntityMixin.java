package svenhjol.charm.mixin;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.BeaconsHealMobs;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity {
    @Shadow private int level;
    @Shadow private StatusEffect primary;
    @Shadow private StatusEffect secondary;

    public BeaconBlockEntityMixin(BlockEntityType<?> type) {
        super(type);
    }

    @Inject(
        method = "applyPlayerEffects",
        at = @At("HEAD")
    )
    private void hookAddEffects(CallbackInfo ci) {
        if (this.world != null)
            BeaconsHealMobs.healInBeaconRange(this.world, this.level, this.pos, this.primary, this.secondary);
    }
}
