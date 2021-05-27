package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import svenhjol.charm.module.CharmModule;

public abstract class CharmLogBlock extends PillarBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmLogBlock(CharmModule module, String name, Settings settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
    }

    public CharmLogBlock(CharmModule module, String name, MapColor fromTop, MapColor fromSide) {
        this(module, name, AbstractBlock.Settings.of(Material.WOOD, state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? fromTop : fromSide)
            .strength(2.0F)
            .sounds(BlockSoundGroup.WOOD));
    }

    public CharmLogBlock(CharmModule module, String name, MapColor color) {
        this(module, name, AbstractBlock.Settings.of(Material.WOOD, color)
            .strength(2.0F)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
