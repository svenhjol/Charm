package svenhjol.charm.module.path_to_dirt;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Right-clicking on a grass path block with a hoe turns it back into dirt.")
public class PathToDirt extends CharmModule {
    @Override
    public void runWhenEnabled() {
        UseBlockCallback.EVENT.register(this::handleUseHoeOnBlock);
    }

    private InteractionResult handleUseHoeOnBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);

        if (level != null && stack.getItem() instanceof HoeItem) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT_PATH) {
                player.swing(hand);

                if (!level.isClientSide) {
                    level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 11);
                    level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // damage the hoe a bit
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
