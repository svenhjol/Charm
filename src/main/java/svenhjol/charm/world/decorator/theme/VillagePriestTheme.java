package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class VillagePriestTheme extends CharmDecoratorTheme
{
    public VillagePriestTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getStorageBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(super.getStorageBlock());
        if (valuable()) states.add(Blocks.ENDER_CHEST.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        if (valuable()) states.add(Blocks.ENCHANTING_TABLE.getDefaultState());

        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.BOOKSHELF.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        if (valuable()) states.add(Blocks.QUARTZ_BLOCK.getDefaultState());
        if (valuable()) states.add(Blocks.LAPIS_BLOCK.getDefaultState());
        if (common()) states.add(Blocks.OBSIDIAN.getDefaultState());

        states.add(Blocks.BOOKSHELF.getDefaultState());
        states.add(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.LIME_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.RED_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.PURPLE_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.ORANGE_GLAZED_TERRACOTTA.getDefaultState());
        states.add(Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        if (valuable()) items.add(Items.CHORUS_FRUIT);
        if (valuable()) items.add(Items.DIAMOND);
        if (valuable()) items.add(Items.ENDER_PEARL);
        if (uncommon()) items.add(Items.EXPERIENCE_BOTTLE);
        if (uncommon()) items.add(Items.BLAZE_POWDER);
        if (common()) items.add(Items.GOLD_INGOT);

        items.add(Items.GOLDEN_AXE);
        items.add(Items.GOLDEN_SHOVEL);
        items.add(Items.GLASS_BOTTLE);
        items.add(Items.CLOCK);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        if (valuable()) locations.add(LootTableList.CHESTS_STRONGHOLD_CROSSING);

        locations.add(CharmLootTables.VILLAGE_PRIEST);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
