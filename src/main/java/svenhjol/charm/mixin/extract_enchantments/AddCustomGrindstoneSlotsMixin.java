package svenhjol.charm.mixin.extract_enchantments;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.module.extract_enchantments.ExtractEnchantments;

@Mixin(value = GrindstoneMenu.class, priority = 1)
public class AddCustomGrindstoneSlotsMixin {
    @Shadow @Final Container repairSlots;

    @Shadow @Final private ContainerLevelAccess access;

    @Shadow @Final private Container resultSlots;

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/GrindstoneMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            ordinal = 0
        )
    )
    private Slot hookAddSlot0(Slot slot) {
        return ExtractEnchantments.getGrindstoneInputSlot(0, this.repairSlots);
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/GrindstoneMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            ordinal = 1
        )
    )
    private Slot hookAddSlot1(Slot slot) {
        return ExtractEnchantments.getGrindstoneInputSlot(1, this.repairSlots);
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/GrindstoneMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            ordinal = 2
        )
    )
    private Slot hookAddSlot2(Slot slot) {
        return ExtractEnchantments.getGrindstoneOutputSlot(this.access, this.repairSlots, this.resultSlots);
    }
}
