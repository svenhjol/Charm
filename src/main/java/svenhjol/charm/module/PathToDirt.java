package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Right-clicking on a grass path block with a hoe turns it back into dirt.")
public class PathToDirt extends MesonModule {
    @Override
    public void initWhenEnabled() {
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            boolean result = convertPath(player, hitResult.getBlockPos(), hand, player.getStackInHand(hand));
            return result ? ActionResult.SUCCESS : ActionResult.PASS;
        }));
    }

    private boolean convertPath(Entity entity, BlockPos pos, Hand hand, ItemStack stack) {
        if (entity.world != null
            && entity instanceof PlayerEntity
            && stack.getItem() instanceof HoeItem
        ) {
            BlockState state = entity.world.getBlockState(pos);
            if (state.getBlock() == Blocks.GRASS_PATH) {
                PlayerEntity player = (PlayerEntity)entity;
                player.swingHand(hand);

                if (!entity.world.isClient) {
                    entity.world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 11);
                    entity.world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    // damage the hoe a bit
                    stack.damage(1, player, p -> p.swingHand(hand));
                    return true;
                }
            }
        }
        return false;
    }
}
