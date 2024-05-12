package svenhjol.charm.feature.hover_sorting.common;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<HoverSorting> {
    public final List<ItemLike> cachedSortables = new ArrayList<>();
    public final List<TagKey<Block>> cachedBlockTags = new ArrayList<>();
    public final List<TagKey<Item>> cachedItemTags = new ArrayList<>();
    public final List<ItemLike> sortables = new ArrayList<>();

    public Registers(HoverSorting feature) {
        super(feature);

        // Client senders
        feature.registry().clientPacketSender(Networking.C2SScrollOnHover.TYPE, Networking.C2SScrollOnHover.CODEC);

        // Server receivers
        feature.registry().packetReceiver(Networking.C2SScrollOnHover.TYPE,
            () -> feature.handlers::scrollOnHover);
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
        ItemHoverSortEvent.INSTANCE.handle(feature().handlers::sortBundle);
        ItemHoverSortEvent.INSTANCE.handle(feature().handlers::sortShulkerBox);
    }
}
