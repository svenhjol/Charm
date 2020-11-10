package svenhjol.charm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmFallingBlock;

public class RedstoneSandBlock extends CharmFallingBlock {
    public RedstoneSandBlock(CharmModule module) {
        super(module, "redstone_sand", FabricBlockSettings
            .of(Material.AGGREGATE)
            .sounds(BlockSoundGroup.SAND)
            .breakByTool(FabricToolTags.SHOVELS)
            .strength(0.5F)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.REDSTONE;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 15;
    }
}
