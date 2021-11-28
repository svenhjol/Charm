package svenhjol.charm.module.shulker_box_tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.client.CharmItemTooltip;
import svenhjol.charm.event.ItemTooltipImageCallback;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.List;
import java.util.Optional;

@ClientModule(module = ShulkerBoxTooltip.class)
public class ShulkerBoxTooltipClient extends CharmModule {
    private static boolean isEnabled = false;

    @Override
    public void runWhenEnabled() {
        ItemTooltipImageCallback.EVENT.register(this::handleItemTooltipImage);
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
        isEnabled = true;
    }

    private void handleRenderTooltip(PoseStack poseStack, ItemStack stack, List<ClientTooltipComponent> components, int x, int y) {
        if (isEnabled && stack != null && ItemHelper.getBlockClass(stack) == ShulkerBoxBlock.class) {
            if (components.size() >= 2) {
                ClientTooltipComponent title = components.get(0);
                ClientTooltipComponent icons = components.get(1);
                components.clear();
                components.add(0, title);
                components.add(1, icons);
            }
        }
    }

    private Optional<TooltipComponent> handleItemTooltipImage(ItemStack stack) {
        if (isEnabled && stack != null && ItemHelper.getBlockClass(stack) == ShulkerBoxBlock.class) {
            CompoundTag shulkerBoxTag = BlockItem.getBlockEntityData(stack);
            if (shulkerBoxTag != null && Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
                BlockEntity blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), shulkerBoxTag);

                if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                    NonNullList<ItemStack> items = shulkerBox.itemStacks;
                    return Optional.of(new CharmItemTooltip(items));
                }
            }
        }

        return Optional.empty();
    }
}
