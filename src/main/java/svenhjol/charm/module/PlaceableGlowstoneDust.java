package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.block.PlacedGlowstoneDustBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;
import svenhjol.meson.mixin.accessor.RenderLayersAccessor;

@Module(description = "Glowstone dust can be placed on the ground as a light source.")
public class PlaceableGlowstoneDust extends MesonModule {
    public static PlacedGlowstoneDustBlock PLACED_GLOWSTONE_DUST;

    @Override
    public void register() {
        PLACED_GLOWSTONE_DUST = new PlacedGlowstoneDustBlock(this);
    }

    @Override
    public void clientRegister() {
        RenderLayersAccessor.getBlocks().put(PLACED_GLOWSTONE_DUST, RenderLayer.getCutout());
    }

    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::tryPlaceDust);
    }

    public static boolean canPlaceAt(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP);
    }

    private ActionResult tryPlaceDust(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getStackInHand(hand);

        if (world != null && stack.getItem() == Items.GLOWSTONE_DUST) {
            BlockState state = world.getBlockState(pos);
            BlockState stateUp = world.getBlockState(pos.up());

            if (state.isOpaque() && stateUp.isAir()) {
                player.swingHand(hand);

                if (!world.isClient) {
                    world.setBlockState(pos.up(), PLACED_GLOWSTONE_DUST.getDefaultState(), 11);
                    world.playSound(null, pos, SoundEvents.BLOCK_NYLIUM_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    if (!player.isCreative())
                        stack.decrement(1);

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }
}
