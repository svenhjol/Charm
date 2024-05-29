package svenhjol.charm.feature.item_hover_sorting.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.charmony.common.helper.TagHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<ItemHoverSorting> {
    public final List<ItemLike> cachedSortables = new ArrayList<>();
    public final List<TagKey<Block>> cachedBlockTags = new ArrayList<>();
    public final List<TagKey<Item>> cachedItemTags = new ArrayList<>();
    public final List<ItemLike> sortables = new ArrayList<>();

    public Registers(ItemHoverSorting feature) {
        super(feature);

        // Client senders
        feature.registry().clientPacketSender(Networking.C2SScrollOnHover.TYPE, Networking.C2SScrollOnHover.CODEC);

        // Server receivers
        feature.registry().packetReceiver(Networking.C2SScrollOnHover.TYPE,
            () -> feature.handlers::scrollOnHover);
    }

    @Override
    public void onEnabled() {
        ItemHoverSortEvent.INSTANCE.handle(feature().handlers::sortBundle);
        ItemHoverSortEvent.INSTANCE.handle(feature().handlers::sortShulkerBox);
    }

    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        var registryAccess = level.registryAccess();
        List<ItemLike> holder = new ArrayList<>();

        cachedBlockTags.forEach(
            blockTagKey -> holder.addAll(TagHelper.getValues(registryAccess
                .registryOrThrow(blockTagKey.registry()), blockTagKey)));

        cachedItemTags.forEach(
            itemTagKey -> holder.addAll(TagHelper.getValues(registryAccess
                .registryOrThrow(itemTagKey.registry()), itemTagKey)));

        holder.addAll(cachedSortables);

        // Clear all sortables and add collected items back to it.
        sortables.clear();
        holder.forEach(item -> {
            if (!sortables.contains(item)) {
                sortables.add(item);
            }
        });
    }
}
