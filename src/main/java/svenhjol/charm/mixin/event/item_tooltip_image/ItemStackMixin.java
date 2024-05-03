package svenhjol.charm.mixin.event.item_tooltip_image;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.TooltipItemHoverEvent;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
        method = "getTooltipLines",
        at = @At("RETURN")
    )
    private void hookGetTooltipImage(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag,
                                     CallbackInfoReturnable<List<Component>> cir) {
        TooltipItemHoverEvent.INSTANCE.invoke((ItemStack)(Object)this, cir.getReturnValue(), tooltipFlag);
    }
}
