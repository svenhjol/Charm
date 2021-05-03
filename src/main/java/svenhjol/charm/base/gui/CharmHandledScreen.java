package svenhjol.charm.base.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import svenhjol.charm.init.CharmResources;

public class CharmHandledScreen<T extends ScreenHandler> extends AbstractCharmContainerScreen<T> {

    public CharmHandledScreen(int rows, T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, getTextureFromRows(rows));
        this.passEvents = true;
        this.backgroundWidth = 175;
        this.backgroundHeight = 111 + 20 * rows;

    }

    public static <T extends ScreenHandler> ScreenRegistry.Factory<T, CharmHandledScreen<T>> createFactory(int rows) {
        return (handler, inventory, title) -> new CharmHandledScreen<>(rows, handler, inventory, title);
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
        this.textRenderer.draw(matrices, this.displayName.asOrderedText(), 8.0F, (float) backgroundHeight - 94, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (client != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);
            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        }
    }

    private static Identifier getTextureFromRows(int rows) {
        switch (rows) {
            case 1:
                return CharmResources.GUI_9_TEXTURE;
            case 2:
                return CharmResources.GUI_18_TEXTURE;
            default:
                throw new IllegalArgumentException("Unsupported row count " + rows);
        }
    }
}
