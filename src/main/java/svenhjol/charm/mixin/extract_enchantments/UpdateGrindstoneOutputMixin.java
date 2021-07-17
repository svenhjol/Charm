package svenhjol.charm.mixin.extract_enchantments;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.module.extract_enchantments.ExtractEnchantments;

import javax.annotation.Nullable;

@CharmMixin(disableIfModsPresent = {"grindenchantments"})
@Mixin(value = GrindstoneMenu.class, priority = 1)
public class UpdateGrindstoneOutputMixin {
    @Nullable Player player;

    @Shadow @Final Container repairSlots;

    @Shadow @Final private Container resultSlots;

    @Inject(
        method = "createResult",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookUpdateResult(CallbackInfo ci) {
        boolean result = ExtractEnchantments.tryUpdateGrindstoneOutput(this.repairSlots, this.resultSlots, this.player);
        if (result) {
            ((AbstractContainerMenu) (Object) this).broadcastChanges();
            ci.cancel();
        }
    }
}
