package svenhjol.charm.mixin.event.smithing_table;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.SmithingTableEvents;

@SuppressWarnings("UnreachableCode")
@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    @Unique
    private @Nullable Player player;

    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess access) {
        super(menuType, i, inventory, access);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        SmithingTableEvents.remove(player);
    }

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("TAIL")
    )
    private void hookInit(int syncId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        SmithingTableEvents.create((SmithingMenu) (Object) this, inventory.player, inventory,
            inputSlots, resultSlots, access);
        player = inventory.player;
    }

    @Inject(
        method = "createResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCreateResult(CallbackInfo ci) {
        var instance = SmithingTableEvents.instance(player);
        if (instance == null) return;

        if (SmithingTableEvents.CALCULATE_OUTPUT.invoke(instance)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "canMoveIntoInputSlots",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanMoveIntoInputSlots(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var template = getTemplate();

        if (SmithingTableEvents.CAN_PLACE.invoke(template, 0, stack)
            || SmithingTableEvents.CAN_PLACE.invoke(template, 1, stack)
            || SmithingTableEvents.CAN_PLACE.invoke(template, 2, stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "mayPickup",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookMayPickup(Player player, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        var instance = SmithingTableEvents.instance(player);
        if (instance != null) {
            var result = SmithingTableEvents.CAN_TAKE.invoke(instance, player);

            if (result == InteractionResult.SUCCESS) {
                cir.setReturnValue(true);
            } else if (result == InteractionResult.FAIL) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
        method = "onTake",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnTake(Player player, ItemStack stack, CallbackInfo ci) {
        var instance = SmithingTableEvents.instance(player);
        if (instance != null && SmithingTableEvents.ON_TAKE.invoke(instance, player, stack)) {
            ci.cancel();
        }
    }

    @Unique
    private ItemStack getTemplate() {
        return inputSlots.getItem(0);
    }
}
