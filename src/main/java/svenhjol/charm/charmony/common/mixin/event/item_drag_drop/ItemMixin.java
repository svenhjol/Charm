package svenhjol.charm.charmony.common.mixin.event.item_drag_drop;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.event.ItemDragDropEvent;

import javax.annotation.Nullable;

@Mixin(Item.class)
public class ItemMixin {

    @ModifyReturnValue(
            method = "overrideStackedOnOther",
            at = @At("RETURN")
    )
    private boolean hookOverrideStackedOnOther(boolean original,
                                               @Local(argsOnly = true) ItemStack source,
                                               @Local(argsOnly = true) Slot slot,
                                               @Local(argsOnly = true) ClickAction clickAction,
                                               @Local(argsOnly = true) Player player
    ) {
        var result = performStack(ItemDragDropEvent.StackType.STACKED_ON_OTHER, source, slot.getItem(), slot, clickAction, player, null);
        if (result != InteractionResult.PASS) {
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "overrideOtherStackedOnMe",
            at = @At("RETURN")
    )
    private boolean hookOverrideOtherStackedOnMe(boolean original,
                                                 @Local(ordinal = 0, argsOnly = true) ItemStack source,
                                                 @Local(ordinal = 1, argsOnly = true) ItemStack dest,
                                                 @Local(argsOnly = true) Slot slot,
                                                 @Local(argsOnly = true) ClickAction clickAction,
                                                 @Local(argsOnly = true) Player player,
                                                 @Local(argsOnly = true) SlotAccess slotAccess
    ) {
        var result = performStack(ItemDragDropEvent.StackType.STACKED_ON_SELF, source, dest, slot, clickAction, player, slotAccess);
        if (result != InteractionResult.PASS) {
            return true;
        }
        return original;
    }

    @Unique
    private InteractionResult performStack(ItemDragDropEvent.StackType stackType, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess) {
        return ItemDragDropEvent.INSTANCE.invoke(stackType, source, dest, slot, clickAction, player, slotAccess);
    }
}
