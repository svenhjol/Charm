package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.crafting.feature.BookshelfChest;
import svenhjol.charm.world.feature.TotemOfReturning;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class VillageLibrarianTheme extends CharmDecoratorTheme
{
    public VillageLibrarianTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        if (valuable()) states.add(Blocks.ENCHANTING_TABLE.getDefaultState());
        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.FURNACE.getDefaultState());

        if (Charm.hasFeature(BookshelfChest.class)) {
            states.add(BookshelfChest.bookshelfChest.getDefaultState());
        }

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        states.add(Blocks.BOOKSHELF.getDefaultState());
        states.add(Blocks.PLANKS.getDefaultState());
        states.add(Blocks.OAK_STAIRS.getDefaultState());

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        if (Charm.hasFeature(TotemOfReturning.class)) {
            if (rare()) items.add(TotemOfReturning.totem);
        }

        items.add(Items.WRITABLE_BOOK);
        items.add(Items.BOOK);
        items.add(Items.COMPASS);
        items.add(Items.CLOCK);
        items.add(Items.PAPER);
        items.add(Items.MAP);
        items.add(Items.FEATHER);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        if (valuable()) locations.add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
        if (valuable()) locations.add(LootTableList.CHESTS_WOODLAND_MANSION);

        locations.add(CharmLootTables.VILLAGE_LIBRARIAN);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
