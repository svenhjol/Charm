package svenhjol.charm.module.block_of_gunpowder;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.block.CharmFallingBlock;
import svenhjol.charm.loader.CharmModule;

public class GunpowderBlock extends CharmFallingBlock {
    public GunpowderBlock(CharmModule module) {
        super(module, "gunpowder_block", FabricBlockSettings
            .of(Material.SAND)
            .sounds(SoundType.SAND)
            .breakByTool(FabricToolTags.SHOVELS)
            .strength(0.5F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchLava(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchLava(worldIn, pos, state)) {
            super.onPlace(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(Level world, BlockPos pos, BlockState state) {
        boolean lavaBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.relative(facing);
                if (world.getBlockState(below).getMaterial() == Material.LAVA) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            world.globalLevelEvent(2001, pos, Block.getId(world.getBlockState(pos)));
            world.removeBlock(pos, true);
        }

        if (!world.isClientSide)
            BlockOfGunpowder.triggerAdvancementForNearbyPlayers((ServerLevel) world, pos);

        return lavaBelow;
    }
}
