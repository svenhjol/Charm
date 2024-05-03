package svenhjol.charm.feature.extra_tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.screen.TooltipGrid;

import java.util.List;

/**
 * Copypasta from {@link net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip}
 */
public class ShulkerBoxItemTooltip implements ClientTooltipComponent, TooltipComponent, TooltipGrid {
    private static final ResourceLocation BACKGROUND_SPRITE
        = new ResourceLocation("container/bundle/background");
    private static final int MARGIN_Y = 4;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;

    private final List<ItemStack> items;

    public ShulkerBoxItemTooltip(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int gridSizeX() {
        return 9;
    }

    @Override
    public int gridSizeY() {
        return 3;
    }

    @Override
    public int getHeight() {
        return this.backgroundHeight() + MARGIN_Y;
    }

    @Override
    public int getWidth(Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + 2;
    }


    public void renderImage(Font font, int i, int j, GuiGraphics guiGraphics) {
        int k = this.gridSizeX();
        int l = this.gridSizeY();
        guiGraphics.blitSprite(BACKGROUND_SPRITE, i, j, backgroundWidth(), backgroundHeight());
        int m = 0;

        for(int n = 0; n < l; ++n) {
            for(int o = 0; o < k; ++o) {
                int p = i + o * 18 + 1;
                int q = j + n * 20 + 1;
                this.renderSlot(p, q, m++, guiGraphics, font);
            }
        }
    }

    private void renderSlot(int i, int j, int k, GuiGraphics guiGraphics, Font font) {
        if (k >= this.items.size()) {
            blit(guiGraphics, i, j);
        } else {
            ItemStack itemStack = this.items.get(k);
            blit(guiGraphics, i, j);
            guiGraphics.renderItem(itemStack, i + 1, j + 1, k);
            guiGraphics.renderItemDecorations(font, itemStack, i + 1, j + 1);
            if (k == 0) {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics, i + 1, j + 1, 0);
            }
        }
    }

    private void blit(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.blitSprite(Texture.SLOT.sprite, i, j, 0, Texture.SLOT.w, Texture.SLOT.h);
    }

    enum Texture {
        BLOCKED_SLOT(new ResourceLocation("container/bundle/blocked_slot"), 18, 20),
        SLOT(new ResourceLocation("container/bundle/slot"), 18, 20);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(final ResourceLocation resourceLocation, final int j, final int k) {
            this.sprite = resourceLocation;
            this.w = j;
            this.h = k;
        }
    }
}