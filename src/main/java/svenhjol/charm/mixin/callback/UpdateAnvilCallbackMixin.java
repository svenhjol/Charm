package svenhjol.charm.mixin.callback;

import net.minecraft.screen.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.UpdateAnvilCallback;

@Mixin(AnvilMenu.class)
public abstract class UpdateAnvilCallbackMixin extends ItemCombinerMenu {

    @Shadow
    private String newItemName;

    @Shadow @Final
    private DataSlot levelCost;

    @Shadow private int repairItemUsage;

    public UpdateAnvilCallbackMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    /**
     * Fires the {@link UpdateAnvilCallback} event allowing
     * for full control over anvil output slot.
     */
    @Inject(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isDamageable()Z",
            ordinal = 0,
            shift = At.Shift.BEFORE
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookUpdateResultUpdateAnvil(CallbackInfo ci, ItemStack left, int i, int baseCost, int k, ItemStack itemStack2, ItemStack right) {
        InteractionResult result = UpdateAnvilCallback.EVENT.invoker().interact((AnvilMenu)(Object)this, this.player, left, right, this.resultSlots, this.newItemName, baseCost, this::applyUpdateAnvil);
        if (result == InteractionResult.SUCCESS)
            ci.cancel();
    }

    private void applyUpdateAnvil(ItemStack out, int xpCost, int materialCost) {
        resultSlots.setItem(0, out);
        this.levelCost.set(5);
        repairItemUsage = materialCost;
    }
}
