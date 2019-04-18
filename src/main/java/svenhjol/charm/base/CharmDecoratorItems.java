package svenhjol.charm.base;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.charm.crafting.block.BlockLantern;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.meson.decorator.MesonInnerDecorator;
import svenhjol.meson.decorator.MesonDecoratorItems;

import javax.annotation.Nullable;

public class CharmDecoratorItems extends MesonDecoratorItems
{
    public CharmDecoratorItems(MesonInnerDecorator structure)
    {
        super(structure);
    }

    public IBlockState getCake()
    {
        BlockCake cakeType;

        if (Charm.hasFeature(FlavoredCake.class)) {
            cakeType = FlavoredCake.cakes.get(generator.getRand().nextInt(FlavoredCake.cakes.size()));
        } else {
            cakeType = (BlockCake) Blocks.CAKE;
        }

        return cakeType.getDefaultState().withProperty(BlockCake.BITES, generator.getRand().nextInt(2));
    }

    public void addStand(int x, int y, int z, EnumFacing facing, @Nullable IBlockState onTop)
    {
        if (onTop == null) {
            switch (rand.nextInt(2)) {
                case 0: onTop = getCake(); break;
                case 1: onTop = getFlowerPot(); break;
                case 2: onTop = getLantern(); break;
            }
        }
        super.addStand(x, y, z, facing, onTop);
    }

    public void addLantern(int x, int y, int z, boolean hanging)
    {
        if (!Charm.hasFeature(Lantern.class)) return;
        generator.add(Lantern.getDefaultLantern().getDefaultState().withProperty(BlockLantern.HANGING, hanging), x, y, z, EnumFacing.NORTH);
    }

    public IBlockState getLantern()
    {
        IBlockState state;
        if (Charm.hasFeature(Lantern.class)) {
            state = Lantern.getDefaultLantern().getDefaultState();
        } else {
            state = Blocks.TORCH.getDefaultState();
        }
        return state;
    }
}
