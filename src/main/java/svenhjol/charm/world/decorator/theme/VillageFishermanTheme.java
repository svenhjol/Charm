package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class VillageFishermanTheme extends CharmDecoratorTheme
{
    public VillageFishermanTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());
        states.add(Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, getRand().nextInt(3)));

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            states.add(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byDyeDamage(i)));
        }

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        items.add(Items.FISH);
        items.add(Items.COOKED_FISH);
        items.add(Items.STRING);
        items.add(Items.LEATHER_BOOTS);
        if (common()) items.add(Items.FISHING_ROD);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        locations.add(CharmLootTables.VILLAGE_FISHERMAN);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
