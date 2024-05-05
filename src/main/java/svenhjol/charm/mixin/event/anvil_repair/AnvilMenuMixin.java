package svenhjol.charm.mixin.event.anvil_repair;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.api.event.AnvilRepairEvent;

import javax.annotation.Nullable;

@SuppressWarnings("UnreachableCode")
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    /**
     * Fires the {@link AnvilRepairEvent} event.
     * Allows intervention when checking if the anvil item can be
     * repaired with another item via the CheckAnvilRepairEvent event.
     * For example, elytra cannot normally be repaired with leather,
     * but using the player.world we can check if insomnia is disabled
     * and therefore allow this repair check.  The reason we don't
     * hook into ElytraItem's canRepair method directly is because
     * there is no world reference.
     */
    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean hookUpdateResultCanRepair(Item leftItem, ItemStack leftStack, ItemStack rightStack) {
        boolean result = AnvilRepairEvent.INSTANCE.invoke((AnvilMenu) (Object) this, this.player, leftStack, rightStack);
        return result || leftItem.isValidRepairItem(leftStack, rightStack);
    }
}
