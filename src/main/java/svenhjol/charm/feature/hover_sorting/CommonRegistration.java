package svenhjol.charm.feature.hover_sorting;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.api.iface.IHoverSortableItemProvider;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingHandler;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.helper.ApiHelper;
import svenhjol.charm.foundation.helper.TagHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class CommonRegistration extends Registration<HoverSorting> {
    List<ItemLike> cachedSortables = new ArrayList<>();
    List<TagKey<Block>> cachedBlockTags = new ArrayList<>();
    List<TagKey<Item>> cachedItemTags = new ArrayList<>();
    List<ItemLike> sortables = new ArrayList<>();

    public CommonRegistration(HoverSorting feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ApiHelper.consume(IHoverSortableItemProvider.class,
            provider -> {
                cachedSortables.addAll(provider.getHoverSortableItems());
                cachedBlockTags.addAll(provider.getHoverSortableBlockTags());
                cachedItemTags.addAll(provider.getHoverSortableItemTags());
            });
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
        ItemHoverSortEvent.INSTANCE.handle(this::handleBundleSorting);
        ItemHoverSortEvent.INSTANCE.handle(this::handleShulkerBoxSorting);
    }

    private void handleBundleSorting(ServerPlayer player, ItemStack stack, SortDirection direction) {
        if (stack.is(Items.BUNDLE) && sortables.contains(Items.BUNDLE)) {
            var bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (bundleContents == null) return;

            List<ItemStack> contents = new LinkedList<>();
            bundleContents.itemsCopy().forEach(contents::add);
            if (contents.isEmpty()) return;

            ItemHoverSortEvent.sortByScrollDirection(contents, direction);
            var newContents = new BundleContents(contents);

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents);
            HoverSorting.triggerSortedItems(player);
        }
    }

    private void handleShulkerBoxSorting(ServerPlayer player, ItemStack stack, SortDirection direction) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock block && sortables.contains(block)) {
            var data = stack.get(DataComponents.CONTAINER);

            if (data != null) {
                // Merge and sort.
                var stacks = new ArrayList<>(data.stream().toList());
                InventoryTidyingHandler.mergeStacks(stacks);
                ItemHoverSortEvent.sortByScrollDirection(stacks, direction);

                // Write contents back to shulker box.
                var itemContainerContents = ItemContainerContents.fromItems(stacks);
                stack.set(DataComponents.CONTAINER, itemContainerContents);

                HoverSorting.triggerSortedItems(player);
            }
        }
    }

    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
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
}
