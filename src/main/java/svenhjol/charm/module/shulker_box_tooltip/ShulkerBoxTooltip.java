package svenhjol.charm.module.shulker_box_tooltip;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.hover_sorting.HoverSorting;
import svenhjol.charm.module.hover_sorting.event.HoverSortItemsCallback;
import svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler;

import java.util.ArrayList;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID)
public class ShulkerBoxTooltip extends CharmModule {
    @Override
    public void runWhenEnabled() {
        ServerWorldEvents.LOAD.register(this::handleWorldLoad);
        HoverSorting.SORT_HANDLERS.add((player, stack, direction) -> {
            if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
                CompoundTag shulkerBoxTag = BlockItem.getBlockEntityData(stack);
                BlockEntity blockEntity;

                if (shulkerBoxTag == null) {
                    // generate a new empty blockentity
                    blockEntity = shulkerBoxBlock.newBlockEntity(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState());
                } else {
                    // instantiate existing shulkerbox blockentity from BlockEntityTag
                    blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), shulkerBoxTag);
                }

                if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                    if (shulkerBox.itemStacks.size() < 1) return;

                    List<ItemStack> stacks = new ArrayList<>(shulkerBox.itemStacks);
                    InventoryTidyingHandler.mergeStacks(stacks);

                    HoverSortItemsCallback.sortByScrollDirection(stacks, direction);
                    NonNullList<ItemStack> nonNullList = NonNullList.create();
                    nonNullList.addAll(stacks);
                    shulkerBox.itemStacks = nonNullList;
                    shulkerBox.saveToItem(stack);
                }
            }
        });
    }

    /**
     * Tagged blocks must be registered at world load.
     */
    private void handleWorldLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            for (Block block : BlockTags.SHULKER_BOXES.getValues()) {
                if (!HoverSorting.SORTABLE.contains(block)) {
                    HoverSorting.SORTABLE.add(block);
                }
            }
        }
    }
}
