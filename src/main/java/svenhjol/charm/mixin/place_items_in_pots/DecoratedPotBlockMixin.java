package svenhjol.charm.mixin.place_items_in_pots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.place_items_in_pots.PlaceItemsInPots;

import java.util.ArrayList;
import java.util.List;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BaseEntityBlock {
    protected DecoratedPotBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Add the blockentity item to the list of drops calculated from the loot table.
     * Vanilla method super.getDrops() is deprecated (?!) hence the suppression.
     */
    @SuppressWarnings("deprecation")
    @Inject(
        method = "getDrops",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetDrops(BlockState state, LootParams.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        var blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof DecoratedPotBlockEntity pot) {
            var opt = PlaceItemsInPots.getItemStack(pot);
            if (opt.isPresent()) {
                var drops = new ArrayList<>(super.getDrops(state, builder));
                drops.add(opt.get());
                cir.setReturnValue(drops);
            }
        }
    }
}
