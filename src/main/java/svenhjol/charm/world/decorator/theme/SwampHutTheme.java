package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class SwampHutTheme extends CharmDecoratorTheme
{
    public SwampHutTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());
        states.add(Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, getRand().nextInt(3) + 1));

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        return LootHelper.getRandomLootTable(LootHelper.RARITY.COMMON, LootHelper.TYPE.POTION);
    }
}
