package svenhjol.charm.block;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import svenhjol.charm.module.Candles;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;

import java.util.Random;

public class CandleBlock extends CharmBlock implements Waterloggable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 9.0D, 10.0D);
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final ParticleEffect flame;

    public CandleBlock(CharmModule module) {
        super(module, "candle", AbstractBlock.Settings
            .of(Material.ORGANIC_PRODUCT)
            .sounds(BlockSoundGroup.WOOL)
            .luminance(s -> s.get(LIT) ? Candles.lightLevel : 0)
            .strength(0.5F));

        this.flame = ParticleTypes.FLAME;
        this.setDefaultState(getDefaultState().with(LIT, Candles.litWhenPlaced).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getBlockPos());
        boolean flag = fluidstate.getFluid() == Fluids.WATER;
        return super.getPlacementState(context).with(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {
        ItemStack held = player.getStackInHand(handIn);
        if (held.getItem() == Items.FLINT_AND_STEEL
            && !state.get(LIT)
            && !state.get(WATERLOGGED)
        ) {
            worldIn.setBlockState(pos, state.with(LIT, true));
            worldIn.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getFluidTickScheduler().schedule(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return facing == Direction.DOWN && !this.canPlaceAt(stateIn, worldIn, currentPos)
            ? Blocks.AIR.getDefaultState()
            : super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.get(Properties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            worldIn.setBlockState(pos, state.with(WATERLOGGED, true).with(LIT, false), 3);
            worldIn.getFluidTickScheduler().schedule(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        return Block.sideCoversSmallSquare(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(CandleBlock.LIT) && !state.get(CandleBlock.WATERLOGGED)) {
            double d0 = (double)pos.getX() + 0.48D;
            double d1 = (double)pos.getY() + 0.68D;
            double d2 = (double)pos.getZ() + 0.48D;
            if (rand.nextFloat() < 0.25F)
                world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);

            world.addParticle(this.flame, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED);
    }
}
