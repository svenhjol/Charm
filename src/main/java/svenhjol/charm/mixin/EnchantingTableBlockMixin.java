package svenhjol.charm.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.base.helper.EnchantmentsHelper;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    /**
     * When rendering a rune particle on the client, this hook redirects
     * the default check of `isOf(Blocks.BOOKSHELF)` to check
     * EnchantmentsHelper.ENCHANTING_BLOCKS.
     *
     * If present, returns true to enable the rune particle to be rendered.
     * Falls back to vanilla behavior if not.
     */
    @Redirect(
        method = "randomDisplayTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
        )
    )
    private boolean hookRandomDisplayTick(BlockState state, Block block) {
        return EnchantmentsHelper.canBlockPowerEnchantingTable(state) || state.isOf(Blocks.BOOKSHELF);
    }
}
