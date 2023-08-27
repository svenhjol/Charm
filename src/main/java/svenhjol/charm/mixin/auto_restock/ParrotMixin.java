package svenhjol.charm.mixin.auto_restock;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.auto_restock.AutoRestock;

@Mixin(Parrot.class)
public class ParrotMixin {
    /**
     * Allows auto restock of an item fed to a parrot.
     */
    @Inject(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V",
            shift = At.Shift.BEFORE
        )
    )
    private void hookMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        AutoRestock.addItemUsedStat(player, player.getItemInHand(hand));
    }
}
