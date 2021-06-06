package svenhjol.charm.module.redstone_sand;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.CharmFallingBlock;

public class RedstoneSandBlock extends CharmFallingBlock {
    public RedstoneSandBlock(CharmModule module) {
        super(module, "redstone_sand", FabricBlockSettings
            .of(Material.SAND)
            .sound(SoundType.SAND)
            .breakByTool(FabricToolTags.SHOVELS)
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
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return 15;
    }
}
