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
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Right-clicking on a grass path block with a hoe turns it back into dirt.")
public class PathToDirt extends CharmCommonModule {
    @Override
    public void run() {
        UseBlockCallback.EVENT.register(this::convertPath);
    }

    private InteractionResult convertPath(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);

        if (world != null && stack.getItem() instanceof HoeItem) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT_PATH) {
                player.swing(hand);

                if (!world.isClientSide) {
                    world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 11);
                    world.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // damage the hoe a bit
                    stack.hurtAndBreak(1, player, p -> p.swing(hand));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
