package svenhjol.charm.mixin.place_items_in_pots;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.place_items_in_pots.PlaceItemsInPots;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {
    @Inject(
        method = "playerWillDestroy",
        at = @At("TAIL")
    )
    private void hookPlayerWillDestroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
        if (level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity pot && !level.isClientSide) {
            PlaceItemsInPots.dropItemStack(level, pos, pot);
        }
    }
}
