package svenhjol.charm.mixin.callback;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.UpdateAnvilCallback;

@Mixin(AnvilScreenHandler.class)
public abstract class UpdateAnvilCallbackMixin extends ForgingScreenHandler {

    @Shadow
    private String newItemName;

    @Shadow @Final
    private Property levelCost;

    @Shadow private int repairItemUsage;

    public UpdateAnvilCallbackMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
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
        ActionResult result = UpdateAnvilCallback.EVENT.invoker().interact((AnvilScreenHandler)(Object)this, this.player, left, right, this.output, this.newItemName, baseCost, this::applyUpdateAnvil);
        if (result == ActionResult.SUCCESS)
            ci.cancel();
    }

    private void applyUpdateAnvil(ItemStack out, int xpCost, int materialCost) {
        output.setStack(0, out);
        this.levelCost.set(5);
        repairItemUsage = materialCost;
    }
}
