package svenhjol.charm.world.decorator.theme;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.base.CharmDecoratorTheme;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonInnerDecorator;

import java.util.ArrayList;
import java.util.List;

public class VillageCarpenterTheme extends CharmDecoratorTheme
{
    public VillageCarpenterTheme(MesonInnerDecorator structure)
    {
        super(structure);
    }

    @Override
    public IBlockState getFunctionalBlock()
    {
        List<IBlockState> states = new ArrayList<>();
        if (rare()) states.add(Blocks.STICKY_PISTON.getDefaultState());
        if (uncommon()) states.add(Blocks.NOTEBLOCK.getDefaultState());
        if (uncommon()) states.add(Blocks.JUKEBOX.getDefaultState());
        states.add(Blocks.CRAFTING_TABLE.getDefaultState());
        states.add(Blocks.BOOKSHELF.getDefaultState());
        states.add(Blocks.PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP));

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public IBlockState getDecorationBlock()
    {
        List<IBlockState> states = new ArrayList<>();

        states.add(Blocks.BOOKSHELF.getDefaultState());
        states.add(Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
        states.add(Blocks.PLANKS.getDefaultState());
        states.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));
        states.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE));
        states.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK));
        states.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH));
        states.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA));
        states.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE));
        states.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE));
        states.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK));
        states.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH));
        states.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA));

        return states.get(getRand().nextInt(states.size()));
    }

    @Override
    public ItemStack getFramedItem()
    {
        List<Item> items = new ArrayList<>();

        if (common() && VillageDecorations.vanillaTools) items.add(Items.IRON_AXE);
        if (common() && VillageDecorations.vanillaTools) items.add(Items.IRON_SHOVEL);
        if (VillageDecorations.vanillaTools) items.add(Items.STONE_AXE);
        if (VillageDecorations.vanillaTools) items.add(Items.STONE_SHOVEL);
        if (VillageDecorations.vanillaTools) items.add(Items.WOODEN_SHOVEL);
        if (VillageDecorations.vanillaTools) items.add(Items.WOODEN_AXE);

        return new ItemStack(items.get(getRand().nextInt(items.size())));
    }

    @Override
    public ResourceLocation getLootTable()
    {
        List<ResourceLocation> locations = new ArrayList<>();

        locations.add(CharmLootTables.VILLAGE_CARPENTER);

        return locations.get(getRand().nextInt(locations.size()));
    }
}
