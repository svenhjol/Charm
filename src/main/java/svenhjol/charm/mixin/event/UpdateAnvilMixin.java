package svenhjol.charm.mixin.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.api.event.UpdateAnvilCallback;

import java.util.Map;

@Mixin(AnvilMenu.class)
public abstract class UpdateAnvilMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;

    @Shadow private int repairItemCountCost;

    public UpdateAnvilMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z",
            ordinal = 0,
            shift = At.Shift.BEFORE
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookCreateResult(CallbackInfo ci, ItemStack input, int i, int baseCost, int k, ItemStack left, ItemStack right, Map<?, ?> enchantments, boolean isEnchantedBook) {
        var menu = (AnvilMenu) (Object) this;
        var result = UpdateAnvilCallback.EVENT.invoker().interact(menu, player, left, right, this::setOutput, this::setXpCost, this::setMaterialCost);

        if (result != InteractionResult.PASS) {
            ci.cancel();
        }
    }

    private void setOutput(ItemStack stack) {
        resultSlots.setItem(0, stack);
    }

    private void setXpCost(int xpCost) {
        cost.set(xpCost);
    }

    private void setMaterialCost(int materialCost) {
        repairItemCountCost = materialCost;
    }
}
