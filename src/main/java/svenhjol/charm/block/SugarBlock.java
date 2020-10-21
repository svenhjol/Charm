package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmFallingBlock;

public class SugarBlock extends CharmFallingBlock {
    public SugarBlock(CharmModule module) {
        super(module, "sugar_block", Settings
            .of(Material.AGGREGATE)
            .sounds(BlockSoundGroup.SAND)
            .strength(0.5F)
        );

        setEffectiveTool(ShovelItem.class);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.neighborUpdate(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(World world, BlockPos pos, BlockState state) {
        boolean waterBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.offset(facing);
                if (world.getBlockState(below).getMaterial() == Material.WATER) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            world.syncGlobalEvent(2001, pos, Block.getRawIdFromState(world.getBlockState(pos)));
            world.removeBlock(pos, true);
        }

        return waterBelow;
    }
}
