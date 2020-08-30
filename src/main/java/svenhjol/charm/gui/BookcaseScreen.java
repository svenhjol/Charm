package svenhjol.charm.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import svenhjol.charm.base.CharmResources;

public class BookcaseScreen extends HandledScreen<ScreenHandler> {
    public BookcaseScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = true;
        this.backgroundWidth = 175;
        this.backgroundHeight = 151;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title.asOrderedText(), 8.0F, 6.0F, 4210752);
        this.textRenderer.draw(matrices, this.playerInventory.getDisplayName().asOrderedText(), 8.0F, (float)backgroundHeight - 94, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (client != null) {
            client.getTextureManager().bindTexture(CharmResources.GUI_18_TEXTURE);

            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        }
    }
}
