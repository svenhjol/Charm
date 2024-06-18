package svenhjol.charm.charmony.client.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;

import java.util.List;

public interface CharmTooltip extends TooltipComponent {
    ResourceLocation BACKGROUND_SPRITE = Charm.id("container/bundle/background");
    int MARGIN_Y = 6;
    int SLOT_SIZE_X = 18;
    int SLOT_SIZE_Y = 20;

    int gridSizeX();

    int gridSizeY();

    List<ItemStack> getItems();

    default int backgroundWidth() {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    default int backgroundHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y;
    }

    default void defaultRenderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        var gx = this.gridSizeX();
        var gy = this.gridSizeY();
        guiGraphics.blit(BACKGROUND_SPRITE, x, y, 0, 0, backgroundWidth(), backgroundHeight());
        int m = 0;

        for (int n = 0; n < gy; ++n) {
            for (int o = 0; o < gx; ++o) {
                int p = x + o * 18 + 1;
                int q = y + n * 20 + 1;
                renderSlot(p, q, m++, guiGraphics, font);
            }
        }
    }

    default void renderSlot(int i, int j, int k, GuiGraphics guiGraphics, Font font) {
        if (k >= getItems().size()) {
            blit(guiGraphics, i, j);
        } else {
            var itemStack = getItems().get(k);
            blit(guiGraphics, i, j);
            guiGraphics.renderItem(itemStack, i + 1, j + 1, k);
            guiGraphics.renderItemDecorations(font, itemStack, i + 1, j + 1);
            if (k == 0) {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics, i + 1, j + 1, 0);
            }
        }
    }

    default void blit(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.blit(Texture.SLOT.sprite, i, j, 0, 0, Texture.SLOT.w, Texture.SLOT.h);
    }

    enum Texture {
        BLOCKED_SLOT(Charm.id("container/bundle/blocked_slot"), SLOT_SIZE_X, SLOT_SIZE_Y),
        SLOT(Charm.id("container/bundle/slot"), SLOT_SIZE_X, SLOT_SIZE_Y);

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