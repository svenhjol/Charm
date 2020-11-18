package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.ExtractEnchantments;

import javax.annotation.Nullable;

@Mixin(value = GrindstoneScreenHandler.class, priority = 1)
public abstract class GrindstoneScreenHandlerMixin {
    @Nullable
    PlayerEntity player;

    @Shadow @Final private Inventory input;

    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private Inventory result;

    @Inject(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At("RETURN")
    )
    private void hookGetPlayer(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
            ordinal = 0
        )
    )
    private Slot hookAddSlot0(Slot slot) {
        return ExtractEnchantments.getGrindstoneInputSlot(0, this.input);
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
            ordinal = 1
        )
    )
    private Slot hookAddSlot1(Slot slot) {
        return ExtractEnchantments.getGrindstoneInputSlot(1, this.input);
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
            ordinal = 2
        )
    )
    private Slot hookAddSlot2(Slot slot) {
        return ExtractEnchantments.getGrindstoneOutputSlot(this.context, this.input, this.result);
    }

    @Inject(
        method = "updateResult",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookUpdateResult(CallbackInfo ci) {
        boolean result = ExtractEnchantments.tryUpdateResult(this.input, this.result, this.player);

        if (result) {
            ((ScreenHandler) (Object) this).sendContentUpdates();
            ci.cancel();
        }
    }
}
