package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class VillageButcherTheme extends CharmDecoratorTheme
{
    public VillageButcherTheme(MesonInnerDecorator structure)
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
        states.add(Blocks.HOPPER.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH));
        states.add(Blocks.COAL_BLOCK.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        if (valuable()) items.add(Items.SADDLE);
        if (valuable()) items.add(Items.RABBIT_FOOT);
        if (uncommon()) items.add(Items.IRON_AXE);
        if (uncommon()) items.add(Items.IRON_SWORD);
        if (common()) items.add(Items.LEATHER_HELMET);
        if (common()) items.add(Items.LEATHER_CHESTPLATE);
        if (common()) items.add(Items.LEATHER_LEGGINGS);
        if (common()) items.add(Items.LEATHER_BOOTS);

        items.add(Items.LEATHER);
        items.add(Items.RABBIT_HIDE);
        items.add(Items.BEEF);
        items.add(Items.PORKCHOP);
        items.add(Items.COAL);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        locations.add(CharmLootTables.VILLAGE_BUTCHER);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
