package svenhjol.charm.world.decorator.inner;

import net.minecraft.block.BlockCauldron;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import svenhjol.charm.world.decorator.theme.SwampHutTheme;
import svenhjol.meson.decorator.MesonInnerDecorator;
import svenhjol.meson.decorator.MesonDecoratorTheme;

public class SwampHut extends MesonInnerDecorator
{
    public SwampHut(ComponentScatteredFeaturePieces.SwampHut structure, World world, StructureBoundingBox box)
    {
        super(structure, world, box);
    }

    @Override
    protected Class<? extends MesonDecoratorTheme> getThemeClass()
    {
        return SwampHutTheme.class;
    }

    @Override
    public void generate()
    {
        /* @todo this is supposed to be a black cat */
        items.spawnCat(4, 3, 4, 2, true, null);
        if (common()) items.addStorageBlock(2, 2, 6, EnumFacing.SOUTH);
        if (common()) add(Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3), 4, 2, 6, EnumFacing.SOUTH);

        add(Blocks.AIR.getDefaultState(), 1, 3, 5);
        add(items.getFlowerPot(7), 1, 3, 4);
    }
}
