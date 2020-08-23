package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.AnvilImprovements;
import svenhjol.charm.module.StackableEnchantedBooks;
import svenhjol.meson.Meson;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Shadow
    @Final
    private Property levelCost;

    @Redirect(
        method = "updateResult",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z",
            ordinal = 1
        )
    )
    private boolean hookUpdateResult(PlayerAbilities abilities) {
        return AnvilImprovements.allowTooExpensive() || abilities.creativeMode;
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
        if (Meson.enabled("charm:stackable_enchanted_books"))
            stack = StackableEnchantedBooks.getReducedStack(inv.getStack(index));

        inv.setStack(index, stack);
    }
}
