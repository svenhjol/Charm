package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonFallingBlock;

public class RedstoneSandBlock extends MesonFallingBlock {
    public RedstoneSandBlock(MesonModule module) {
        super(module, "redstone_sand", Block.Settings
            .of(Material.AGGREGATE)
            .sounds(BlockSoundGroup.SAND)
            .strength(0.5F)
        );

        this.setEffectiveTool(ShovelItem.class);
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
