package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class CharmHandledScreen<T extends ScreenHandler> extends HandledScreen<T> {
    protected final Identifier texture;

    public CharmHandledScreen(T screenContainer, PlayerInventory inv, Text titleIn, Identifier texture) {
        super(screenContainer, inv, titleIn);
        this.passEvents = true;
        this.texture = texture;
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
        this.textRenderer.draw(matrices, this.playerInventoryTitle.asOrderedText(), 8.0F, (float) backgroundHeight - 94, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        if (client != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);

            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            this.drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        }
    }
}
