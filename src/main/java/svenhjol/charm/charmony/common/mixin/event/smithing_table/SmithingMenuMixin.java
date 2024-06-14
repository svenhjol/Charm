package svenhjol.charm.charmony.common.mixin.event.smithing_table;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import svenhjol.charm.charmony.event.SmithingTableEvents;

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

    @ModifyReturnValue(
            method = "canMoveIntoInputSlots",
            at = @At("RETURN")
    )
    private boolean hookCanMoveIntoInputSlots(boolean original, @Local(argsOnly = true) ItemStack stack) {
        var template = getTemplate();

        if (SmithingTableEvents.CAN_PLACE.invoke(template, 0, stack)
                || SmithingTableEvents.CAN_PLACE.invoke(template, 1, stack)
                || SmithingTableEvents.CAN_PLACE.invoke(template, 2, stack)
        ) {
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "mayPickup",
            at = @At("RETURN")
    )
    private boolean hookMayPickup(boolean original) {
        var instance = SmithingTableEvents.instance(player);
        if (instance != null) {
            var result = SmithingTableEvents.CAN_TAKE.invoke(instance, player);

            if (result == InteractionResult.SUCCESS) {
                return true;
            } else if (result == InteractionResult.FAIL) {
                return false;
            }
        }
        return original;
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
