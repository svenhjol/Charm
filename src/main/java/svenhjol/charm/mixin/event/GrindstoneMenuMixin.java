package svenhjol.charm.mixin.event;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.GrindstoneEvents;

import javax.annotation.Nullable;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    private @Nullable Player player;

    @Mutable
    @Shadow @Final Container repairSlots;

    @Mutable
    @Shadow @Final private Container resultSlots;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("TAIL")
    )
    private void hookInit(int syncId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        if (inventory.player != null) {
            GrindstoneEvents.create((GrindstoneMenu)(Object)this, inventory.player, inventory, repairSlots, resultSlots, access);
            player = inventory.player;
        }
    }

    @Inject(
        method = "createResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCreateResult(CallbackInfo ci) {
        GrindstoneEvents.GrindstoneMenuInstance instance = GrindstoneEvents.instance(player);
        if (instance == null) return;

        if (GrindstoneEvents.CALCULATE_OUTPUT.invoker().invoke(instance)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "removed",
        at = @At("TAIL")
    )
    private void hookRemoved(Player player, CallbackInfo ci) {
        GrindstoneEvents.remove(player);
    }
}
