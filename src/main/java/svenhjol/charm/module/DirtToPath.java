package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
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

@Module(mod = Charm.MOD_ID, description = "Right-clicking dirt with a shovel turns it into grass path.")
public class DirtToPath extends CharmModule {
    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::convertDirt);
    }

    private ActionResult convertDirt(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getStackInHand(hand);

        if (world != null && stack.getItem() instanceof ShovelItem) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT) {
                player.swingHand(hand);

                if (!world.isClient) {
                    world.setBlockState(pos, Blocks.DIRT_PATH.getDefaultState(), 11);
                    world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    // damage the shovel a bit
                    stack.damage(1, player, p -> p.swingHand(hand));
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
