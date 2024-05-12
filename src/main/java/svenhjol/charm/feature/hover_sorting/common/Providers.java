package svenhjol.charm.feature.hover_sorting.common;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.iface.IHoverSortableItemProvider;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.List;

public final class Providers extends ProviderHolder<HoverSorting> implements IHoverSortableItemProvider {
    public Providers(HoverSorting feature) {
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

    @Override
    public void onEnabled() {
        var registers = feature().registers;

        ApiHelper.consume(IHoverSortableItemProvider.class,
            provider -> {
                registers.cachedSortables.addAll(provider.getHoverSortableItems());
                registers.cachedBlockTags.addAll(provider.getHoverSortableBlockTags());
                registers.cachedItemTags.addAll(provider.getHoverSortableItemTags());
            });
    }
}
