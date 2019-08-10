package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonBlock;

public class GunpowderBlockBlock extends FallingBlock implements IMesonBlock
{
    public GunpowderBlockBlock()
    {
        super(Block.Properties
            .create(Material.SAND)
            .sound(SoundType.SAND)
            .hardnessAndResistance(0.5F)
        );
        setRegistryName(new ResourceLocation(Charm.MOD_ID, "gunpowder_block"));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!tryTouchLava(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (!tryTouchLava(worldIn, pos, state)) {
            super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(World world, BlockPos pos, BlockState state)
    {
        boolean lavaBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.offset(facing);
                if (world.getBlockState(below).getMaterial() == Material.LAVA) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
            world.removeBlock(pos, true);
        }

        return lavaBelow;
    }
}
