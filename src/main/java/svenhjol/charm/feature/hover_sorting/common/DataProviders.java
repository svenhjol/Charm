package svenhjol.charm.feature.hover_sorting.common;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.iface.IHoverSortableItemProvider;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class DataProviders extends FeatureHolder<HoverSorting> implements IHoverSortableItemProvider {
    public DataProviders(HoverSorting feature) {
        super(feature);
    }

    @Override
    public List<ItemLike> getHoverSortableItems() {
        return List.of(Items.BUNDLE);
    }

    @Override
    public List<TagKey<Block>> getHoverSortableBlockTags() {
        return List.of(BlockTags.SHULKER_BOXES);
    }
}
