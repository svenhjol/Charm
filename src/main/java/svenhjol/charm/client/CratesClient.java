package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.OrderedText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.block.CrateBlock;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.gui.CrateScreen;
import svenhjol.charm.handler.TooltipInventoryHandler;
import svenhjol.charm.module.Crates;

import java.util.List;

public class CratesClient extends CharmClientModule {
    public CratesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        if (Crates.showTooltip)
            RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);

        ScreenRegistry.register(Crates.SCREEN_HANDLER, CrateScreen::new);
    }

    private ActionResult handleRenderTooltip(MatrixStack matrices, ItemStack stack, List<? extends OrderedText> lines, int x, int y) {
        if (stack != null && ItemHelper.getBlockClass(stack) == CrateBlock.class) {
            boolean result = renderTooltip(matrices, stack, lines, x, y);
            if (result)
                return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private boolean renderTooltip(MatrixStack matrices, ItemStack stack, List<? extends OrderedText> lines, int tx, int ty) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (!stack.hasTag())
            return false;

        CompoundTag tag = ItemNBTHelper.getCompound(stack, "BlockEntityTag", true);

        if (tag == null)
            return false;

        if (!tag.contains("id", 8)) {
            tag = tag.copy();
            tag.putString("id", "charm:crate");
        }

        int inventorySize = 27; // TODO: should be a constant somewhere
        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        if (tag.contains("Items")) {
            Inventories.fromTag(tag, itemStacks);
        }

        int x = tx - 5;
        int y = ty - 35;
        int w = 172;
        int h = 27;
        int right = x + w;

        if (right > mc.getWindow().getScaledWidth())
            x -= (right - mc.getWindow().getScaledWidth());

        if (y < 0)
            y = ty + lines.size() * 10 + 5;

        RenderSystem.pushMatrix();
        DiffuseLighting.enable();
        RenderSystem.enableRescaleNormal();
        RenderSystem.color3f(1f, 1f, 1f);
        RenderSystem.translatef(0, 0, 700);
        mc.getTextureManager().bindTexture(CharmResources.SLOT_WIDGET);

        DiffuseLighting.disable();
        TooltipInventoryHandler.renderTooltipBackground(mc, matrices, x, y, 9, 1, -1);
        RenderSystem.color3f(1f, 1f, 1f);

        ItemRenderer render = mc.getItemRenderer();
        DiffuseLighting.enable();
        RenderSystem.enableDepthTest();

        for (int i = 0; i < inventorySize; i++) {
            ItemStack itemstack;

            try {
                itemstack = itemStacks.get(i);
            } catch (Exception e) {
                // catch null issue with itemstack. Needs investigation. #255
                continue;
            }
            int xp = x + 6 + (i % 9) * 18;
            int yp = y + 6 + (i / 9) * 18;

            if (!itemstack.isEmpty()) {
                render.renderGuiItemIcon(itemstack, xp, yp);
                render.renderGuiItemOverlay(mc.textRenderer, itemstack, xp, yp);
            }
        }

        RenderSystem.disableDepthTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
        return true;
    }
}

