package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static svenhjol.charm.event.StackItemOnItemEvent.*;
import static svenhjol.charm.event.StackItemOnItemEvent.Direction.*;

@Mixin(Item.class)
public class StackItemOnItemEventMixin {
    @Inject(
        method = "overrideStackedOnOther",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOverrideStackedOnOther(ItemStack source, Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> ci) {
        if (performStack(STACKED_ON_OTHER, source, slot.getItem(), slot, clickAction, player, null))
            ci.setReturnValue(true);
    }

    @Inject(
        method = "overrideOtherStackedOnMe",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOverrideOtherStackedOnMe(ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> ci) {
        if (performStack(STACKED_ON_SELF, source, dest, slot, clickAction, player, slotAccess))
            ci.setReturnValue(true);
    }

    private boolean performStack(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess) {
        return EVENT.invoker().interact(direction, source, dest, slot, clickAction, player, slotAccess);
    }
}
