package svenhjol.charm.module.block_of_gunpowder;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.block.CharmFallingBlock;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.block_of_sugar.BlockOfSugar;

public class GunpowderBlock extends CharmFallingBlock {
    public GunpowderBlock(CharmModule module) {
        super(module, "gunpowder_block", FabricBlockSettings
            .of(Material.SAND)
            .sounds(SoundType.SAND)
            .strength(0.5F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(Level level, BlockPos pos, BlockState state) {
        boolean lavaBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.relative(facing);
                if (level.getBlockState(below).getMaterial() == Material.LAVA) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
            level.removeBlock(pos, true);
            level.playSound(null, pos, BlockOfSugar.SUGAR_DISSOLVE_SOUND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        if (!level.isClientSide) {
            BlockOfGunpowder.triggerAdvancementForNearbyPlayers((ServerLevel) level, pos);
        }

        return lavaBelow;
    }
}
