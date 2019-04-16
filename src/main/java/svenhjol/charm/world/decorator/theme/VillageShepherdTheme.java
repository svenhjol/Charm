package svenhjol.charm.world.decorator.theme;

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

public class VillageShepherdTheme extends CharmDecoratorTheme
{
    public VillageShepherdTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());
        states.add(Blocks.DISPENSER.getDefaultState());

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

        items.add(Item.getItemFromBlock(Blocks.WOOL));
        items.add(Items.STRING);
        items.add(Items.MUTTON);
        items.add(Items.COOKED_MUTTON);
        if (common()) items.add(Items.SHEARS);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        locations.add(CharmLootTables.VILLAGE_SHEPHERD);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
