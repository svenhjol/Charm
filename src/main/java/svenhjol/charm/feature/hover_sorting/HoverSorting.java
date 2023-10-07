package svenhjol.charm.feature.hover_sorting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.hover_sorting.HoverSortingNetwork.ScrollOnHover;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingHandler;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.TagHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.ItemHoverSortEvent;
import svenhjol.charmony_api.event.LevelLoadEvent;
import svenhjol.charmony_api.iface.IHoverSortableItemProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Feature(mod = Charm.MOD_ID, description = "Scroll the mouse while hovering over a bundle or shulker box to cycle the order of its contents.")
public class HoverSorting extends CharmonyFeature implements IHoverSortableItemProvider {
    private final List<ItemLike> cachedSortables = new ArrayList<>();
    private final List<TagKey<Block>> cachedBlockTags = new ArrayList<>();
    private final List<TagKey<Item>> cachedItemTags = new ArrayList<>();
    private final List<ItemLike> sortables = new ArrayList<>();

    @Override
    public void register() {
        ApiHelper.consume(IHoverSortableItemProvider.class,
            provider -> {
                cachedSortables.addAll(provider.getHoverSortableItems());
                cachedBlockTags.addAll(provider.getHoverSortableBlockTags());
                cachedItemTags.addAll(provider.getHoverSortableItemTags());
            });

        HoverSortingNetwork.register();
        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
        ItemHoverSortEvent.INSTANCE.handle(this::handleBundleSorting);
        ItemHoverSortEvent.INSTANCE.handle(this::handleShulkerBoxSorting);
    }

    /**
     * Network callback from the client scroll event.
     * @see ScrollOnHover
     */
    public static void handleScrollOnHover(ScrollOnHover message, Player player) {
        var container = player.containerMenu;
        var slotIndex = message.getSlotIndex();
        var direction = message.getSortDirection();

        // Check that the slot index is not out of bounds.
        if (slotIndex >= container.slots.size() || slotIndex < 0) return;

        var itemInSlot = container.getSlot(slotIndex).getItem();

        // Invoke the hover sort event on the hovered item.
        ItemHoverSortEvent.INSTANCE.invoke((ServerPlayer)player, itemInSlot, direction);
    }

    private void handleBundleSorting(ServerPlayer player, ItemStack stack, ItemHoverSortEvent.SortDirection direction) {
        if (stack.is(Items.BUNDLE) && sortables.contains(Items.BUNDLE)) {
            var itemsTag = BundleItem.TAG_ITEMS;
            var contents = BundleItem.getContents(stack)
                .collect(Collectors.toCollection(LinkedList::new));

            if (contents.isEmpty()) {
                return;
            }

            ItemHoverSortEvent.sortByScrollDirection(contents, direction);
            stack.removeTagKey(itemsTag);

            var tag = stack.getOrCreateTag();
            tag.put(itemsTag, new ListTag());
            var list = tag.getList(itemsTag, 10);

            Collections.reverse(contents);
            contents.forEach(itemStack -> {
                var t = new CompoundTag();
                itemStack.save(t);
                list.add(0, t);
            });
        }
    }

    private void handleShulkerBoxSorting(ServerPlayer player, ItemStack stack, ItemHoverSortEvent.SortDirection direction) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock block && sortables.contains(block)) {
            var tag = BlockItem.getBlockEntityData(stack);
            BlockEntity blockEntity;

            if (tag == null) {
                // Generate a new empty block entity.
                blockEntity = block.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            } else {
                // Instantiate existing shulkerbox block entity from its tag.
                blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, block.defaultBlockState(), tag);
            }

            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                var itemStacks = shulkerBox.itemStacks;
                if (itemStacks.isEmpty()) return;

                // Merge and sort contents.
                var stacks = new ArrayList<>(itemStacks);
                InventoryTidyingHandler.mergeStacks(stacks);
                ItemHoverSortEvent.sortByScrollDirection(stacks, direction);

                // Write contents back to shulker box.
                NonNullList<ItemStack> list = NonNullList.create();
                list.addAll(stacks);
                shulkerBox.itemStacks = list;
                shulkerBox.saveToItem(stack);
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

    @Override
    public List<ItemLike> getHoverSortableItems() {
        return List.of(Items.BUNDLE);
    }

    @Override
    public List<TagKey<Block>> getHoverSortableBlockTags() {
        return List.of(BlockTags.SHULKER_BOXES);
    }
}
