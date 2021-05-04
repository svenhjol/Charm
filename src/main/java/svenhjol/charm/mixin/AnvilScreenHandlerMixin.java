package svenhjol.charm.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.util.ActionResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.event.CheckAnvilRepairCallback;
import svenhjol.charm.event.TakeAnvilOutputCallback;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.module.AnvilImprovements;
import svenhjol.charm.module.StackableEnchantedBooks;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    @Shadow private String newItemName;

    @Shadow private int repairItemUsage;

    public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z",
            ordinal = 1
        )
    )
    private boolean hookUpdateResultTooExpensive(PlayerAbilities abilities) {
        return AnvilImprovements.allowTooExpensive() || abilities.creativeMode;
    }

    /**
     * Allows intervention when checking if the anvil item can be
     * repaired with another item via the CheckAnvilRepairCallback event.
     *
     * For example, elytra cannot normally be repaired with leather,
     * but using the player.world we can check if insomnia is disabled
     * and therefore allow this repair check.  The reason we don't
     * hook into ElytraItem's canRepair method directly is because
     * there is no world reference.
     */
    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;canRepair(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean hookUpdateResultCanRepair(Item leftItem, ItemStack leftStack, ItemStack rightStack) {
        return CheckAnvilRepairCallback.EVENT.invoker().interact((AnvilScreenHandler)(Object) this, this.player, leftStack, rightStack);
    }

    /**
     * Allows an event to completely override the normal anvil
     * input and output process via the UpdateAnvilCallback event.
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
        if (result == ActionResult.SUCCESS) {
            ci.cancel();
        }
    }

    private void applyUpdateAnvil(ItemStack out, int xpCost, int materialCost) {
        output.setStack(0, out);
        this.levelCost.set(5);
        repairItemUsage = materialCost;
    }

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private void hookUpdateResultAllowHigherLevel(Map<Enchantment, Integer> enchantments, ItemStack outputStack) {
        if (!ModuleHandler.enabled(AnvilImprovements.class) || !AnvilImprovements.higherEnchantmentLevels) {
            EnchantmentHelper.set(enchantments, outputStack); // vanilla behavior
            return;
        }

        ItemStack inputStack = this.input.getStack(1);
        AnvilImprovements.setEnchantmentsAllowHighLevel(enchantments, inputStack, outputStack);
    }

    @Inject(
        method = "canTakeOutput",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanTakeOutput(PlayerEntity player, boolean unused, CallbackInfoReturnable<Boolean> cir) {
        if (AnvilImprovements.allowTakeWithoutXp(player, levelCost))
            cir.setReturnValue(true);
    }

    @Inject(
        method = "onTakeOutput",
        at = @At("HEAD")
    )
    private void hookOnTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        TakeAnvilOutputCallback.EVENT.invoker().interact((AnvilScreenHandler)(Object)this, player, stack);
    }

    @Redirect(
        method = "onTakeOutput",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V",
            opcode = Opcodes.INVOKEINTERFACE,
            ordinal = 2
        )
    )
    private void anvilUpdateHook(Inventory inv, int index, ItemStack stack) {
        if (ModuleHandler.enabled("charm:stackable_enchanted_books"))
            stack = StackableEnchantedBooks.getReducedStack(inv.getStack(index));

        inv.setStack(index, stack);
    }
}
