package svenhjol.charm.module.redstone_sand;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.block.CharmFallingBlock;
import svenhjol.charm.loader.CharmModule;

public class RedstoneSandBlock extends CharmFallingBlock {
    public RedstoneSandBlock(CharmModule module) {
        super(module, "redstone_sand", FabricBlockSettings
            .of(Material.SAND)
            .sounds(SoundType.SAND)
            .strength(0.5F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_REDSTONE;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }
}
