package svenhjol.charm.base;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.block.BlockFlavoredCake;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.charm.crafting.block.BlockLantern;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.charm.world.compat.FutureMcBlocks;
import svenhjol.meson.decorator.MesonDecoratorItems;
import svenhjol.meson.decorator.MesonInnerDecorator;

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
            Object[] values = FlavoredCake.cakeTypes.values().toArray();
            cakeType = (BlockFlavoredCake) values[generator.getRand().nextInt(values.length)];
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
        if (Lantern.ironLantern == null && FutureMcBlocks.lanternHangingProperty != null)
            generator.add(FutureMcBlocks.lantern.getDefaultState().withProperty(FutureMcBlocks.lanternHangingProperty, hanging), x, y, z, EnumFacing.NORTH);
        else if (Charm.hasFeature(Lantern.class))
            generator.add(Lantern.getDefaultLantern().getDefaultState().withProperty(BlockLantern.HANGING, hanging), x, y, z, EnumFacing.NORTH);
    }

    public IBlockState getLantern()
    {
        IBlockState state;
        if (Lantern.ironLantern == null && FutureMcBlocks.lanternHangingProperty != null) {
            state = FutureMcBlocks.lantern.getDefaultState().withProperty(FutureMcBlocks.lanternHangingProperty, false);
        } else if (Charm.hasFeature(Lantern.class)) {
            state = Lantern.getDefaultLantern().getDefaultState();
        } else {
            state = Blocks.TORCH.getDefaultState();
        }
        return state;
    }
}
