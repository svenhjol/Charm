package svenhjol.charm.mixin.helper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.init.CharmTags;

@Mixin(EnchantmentTableBlock.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public class ShowEnchantmentParticlesMixin {
    @Redirect(
        method = "animateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean hookAnimateTick(BlockState state, Block block) {
        Block compare = state.getBlock();
        return compare == block || CharmTags.PROVIDE_ENCHANTING_POWER.contains(compare);
    }
}
