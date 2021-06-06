package svenhjol.charm.module.shulker_box_tooltips;

import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.helper.TooltipHelper;
import svenhjol.charm.mixin.accessor.ShulkerBoxBlockEntityAccessor;

import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;

public class ShulkerBoxTooltipsClient extends CharmClientModule {
    public ShulkerBoxTooltipsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && ItemHelper.getBlockClass(stack) == ShulkerBoxBlock.class) {
            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int tx, int ty) {
        if (stack == null || !stack.hasTag())
            return;

        CompoundTag nbt = ItemNBTHelper.getCompound(stack, "BlockEntityTag", true);

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
        NonNullList<ItemStack> items = ((ShulkerBoxBlockEntityAccessor)shulkerbox).getInventory();
        if (items.stream().allMatch(ItemStack::isEmpty))
            return;

        TooltipHelper.renderOverlay(matrices, items, lines, tx, ty);
    }
}
