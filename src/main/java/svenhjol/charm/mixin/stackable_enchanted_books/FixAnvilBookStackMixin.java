package svenhjol.charm.mixin.stackable_enchanted_books;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.stackable_enchanted_books.StackableEnchantedBooks;

@Mixin(AnvilScreenHandler.class)
public class FixAnvilBookStackMixin {

    /**
     * When adding a stack of enchanted books to an item on the anvil,
     * the entire stack is lost.  The vanilla logic does not take into
     * account an enchanted book stack.  This mixin calls getReducedStack
     * to properly decrement the stack, then sets the updated stack
     * back to the slot.
     */
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
