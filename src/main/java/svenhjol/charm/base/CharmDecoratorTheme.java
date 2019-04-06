package svenhjol.charm.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Barrel;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.meson.decorator.MesonInnerDecorator;
import svenhjol.meson.decorator.MesonDecoratorTheme;

import java.util.ArrayList;
import java.util.List;

public class CharmDecoratorTheme extends MesonDecoratorTheme
{
    public CharmDecoratorTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getStorageBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        if (Charm.hasFeature(Crate.class)) states.add(Crate.crate.getDefaultState());
        if (Charm.hasFeature(Barrel.class)) states.add(Barrel.barrel.getDefaultState());
        states.add(Blocks.CHEST.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }
}
