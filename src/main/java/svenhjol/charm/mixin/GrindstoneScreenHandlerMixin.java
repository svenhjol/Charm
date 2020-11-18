package svenhjol.charm.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.module.ExtractEnchantments;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
    @Shadow @Final private Inventory input;

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
            ordinal = 0
        )
    )
    private Slot hookAddSlot0(Slot slot) {
        return ExtractEnchantments.getGrindstoneSlot(0, this.input);
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
        return ExtractEnchantments.getGrindstoneSlot(1, this.input);
    }
}
