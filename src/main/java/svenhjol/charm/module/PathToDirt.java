package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Right-clicking on a grass path block with a hoe turns it back into dirt.")
public class PathToDirt extends CharmModule {
    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::convertPath);
    }

    private ActionResult convertPath(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getStackInHand(hand);

        if (world != null && stack.getItem() instanceof HoeItem) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT_PATH) {
                player.swingHand(hand);

                if (!world.isClient) {
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 11);
                    world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    // damage the hoe a bit
                    stack.damage(1, player, p -> p.swingHand(hand));
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
