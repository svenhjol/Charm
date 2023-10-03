package svenhjol.charm.mixin.place_items_in_pots;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.place_items_in_pots.PlaceItemsInPots;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityMixin {
    @Unique
    private ItemStack itemStack;

    @Inject(
        method = "load",
        at = @At("TAIL")
    )
    private void hookLoad(CompoundTag tag, CallbackInfo ci) {
        if (!tag.contains(PlaceItemsInPots.ITEM_STACK_TAG)) return;

        var itemTag = tag.getCompound(PlaceItemsInPots.ITEM_STACK_TAG);
        itemStack = ItemStack.of(itemTag);
    }

    @Inject(
        method = "saveAdditional",
        at = @At("TAIL")
    )
    private void hookSaveAdditional(CompoundTag tag, CallbackInfo ci) {
        if (itemStack == null || itemStack.isEmpty()) return;

        var itemTag = new CompoundTag();
        itemStack.save(itemTag);
        tag.put(PlaceItemsInPots.ITEM_STACK_TAG, itemTag);
    }
}
