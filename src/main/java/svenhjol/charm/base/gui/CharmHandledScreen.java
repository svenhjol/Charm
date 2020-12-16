package svenhjol.charm.base.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmResources;

public class CharmHandledScreen<T extends ScreenHandler> extends HandledScreen<T> {
    private final Identifier texture;

    public CharmHandledScreen(int rows, T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = true;
        this.backgroundWidth = 175;
        this.backgroundHeight = 111 + 20 * rows;
        switch (rows) {
            case 1:
                texture = CharmResources.GUI_9_TEXTURE;
                break;
            case 2:
                texture = CharmResources.GUI_18_TEXTURE;
                break;
            default:
                throw new IllegalArgumentException("Unsupported row count " + rows);
        }
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
        this.textRenderer.draw(matrices, this.playerInventory.getDisplayName().asOrderedText(), 8.0F, (float) backgroundHeight - 94, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (client != null) {
            client.getTextureManager().bindTexture(texture);

            int x = (width - backgroundWidth) / 2;
            int y = (height - backgroundHeight) / 2;
            drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        }
    }
}
