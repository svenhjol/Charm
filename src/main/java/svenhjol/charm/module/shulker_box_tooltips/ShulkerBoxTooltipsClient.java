package svenhjol.charm.module.shulker_box_tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.event.RenderTooltipEvent;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.helper.TooltipHelper;
import svenhjol.charm.mixin.accessor.ShulkerBoxBlockEntityAccessor;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;

public class ShulkerBoxTooltipsClient extends CharmModule {
    @Override
    public void runWhenEnabled() {
        RenderTooltipEvent.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && ItemHelper.getBlockClass(stack) == ShulkerBoxBlock.class) {
            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int tx, int ty) {
        if (stack == null || !stack.hasTag())
            return;

        CompoundTag nbt = ItemNbtHelper.getCompound(stack, "BlockEntityTag", true);

        if (nbt == null)
            return;

        if (!nbt.contains("id", 8)) {
            nbt = nbt.copy();
            nbt.putString("id", "minecraft:shulker_box");
        }
        BlockItem blockItem = (BlockItem) stack.getItem();
        BlockEntity blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, blockItem.getBlock().defaultBlockState(), nbt);
        if (blockEntity == null)
            return;

        ShulkerBoxBlockEntity shulkerbox = (ShulkerBoxBlockEntity) blockEntity;
        NonNullList<ItemStack> items = ((ShulkerBoxBlockEntityAccessor)shulkerbox).getItemStacks();
        if (items.stream().allMatch(ItemStack::isEmpty))
            return;

        TooltipHelper.renderOverlay(matrices, items, lines, tx, ty);
    }
}
