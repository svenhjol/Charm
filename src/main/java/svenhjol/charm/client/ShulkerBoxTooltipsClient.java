package svenhjol.charm.client;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.handler.TooltipInventoryHandler;
import svenhjol.charm.mixin.accessor.ShulkerBoxBlockEntityAccessor;

import javax.annotation.Nullable;
import java.util.List;

public class ShulkerBoxTooltipsClient extends CharmClientModule {
    public ShulkerBoxTooltipsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<TooltipComponent> lines, int x, int y) {
        if (stack != null && ItemHelper.getBlockClass(stack) == ShulkerBoxBlock.class) {
            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<TooltipComponent> lines, int tx, int ty) {
        if (stack == null || !stack.hasTag())
            return;

        NbtCompound nbt = ItemNBTHelper.getCompound(stack, "BlockEntityTag", true);

        if (nbt == null)
            return;

        if (!nbt.contains("id", 8)) {
            nbt = nbt.copy();
            nbt.putString("id", "minecraft:shulker_box");
        }
        BlockItem blockItem = (BlockItem) stack.getItem();
        BlockEntity blockEntity = BlockEntity.createFromNbt(BlockPos.ORIGIN, blockItem.getBlock().getDefaultState(), nbt);
        if (blockEntity == null)
            return;

        ShulkerBoxBlockEntity shulkerbox = (ShulkerBoxBlockEntity) blockEntity;
        DefaultedList<ItemStack> items = ((ShulkerBoxBlockEntityAccessor)shulkerbox).getInventory();
        if (items.stream().allMatch(ItemStack::isEmpty))
            return;

        TooltipInventoryHandler.renderOverlay(matrices, items, lines, tx, ty);
    }
}
