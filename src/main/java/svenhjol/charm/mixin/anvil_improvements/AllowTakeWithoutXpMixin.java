package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

@Mixin(AnvilScreenHandler.class)
public class AllowTakeWithoutXpMixin {
    @Shadow
    @Final
    private Property levelCost;

    /**
     * Vanilla doesn't allow taking an item from the anvil with a zero XP cost.
     * The return value from the method is false by default.
     *
     * This hook checks the AnvilImprovements module config option and returns true if set.
     */
    @Inject(
        method = "canTakeOutput",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanTakeOutput(PlayerEntity player, boolean unused, CallbackInfoReturnable<Boolean> cir) {
        if (AnvilImprovements.allowTakeWithoutXp(player, levelCost))
            cir.setReturnValue(true);
    }
}
