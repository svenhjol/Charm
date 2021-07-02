package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.init.CharmResources;

@SuppressWarnings({"unused"})
public class CharmInventoryScreen<T extends AbstractContainerMenu> extends CharmHandledScreen<T> {

    public CharmInventoryScreen(int rows, T handler, Inventory inventory, Component title) {
        super(handler, inventory, title, getTextureFromRows(rows));
        this.passEvents = true;
        this.imageWidth = 175;
        this.imageHeight = 111 + 20 * rows;

    }

    public static <T extends AbstractContainerMenu> ScreenRegistry.Factory<T, CharmInventoryScreen<T>> createFactory(int rows) {
        return (handler, inventory, title) -> new CharmInventoryScreen<>(rows, handler, inventory, title);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        this.font.draw(matrices, this.title.getVisualOrderText(), 8.0F, 6.0F, 4210752);
        this.font.draw(matrices, this.playerInventoryTitle.getVisualOrderText(), 8.0F, (float) imageHeight - 94, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (minecraft != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            blit(matrices, x, y, 0, 0, imageWidth, imageHeight);
        }
    }

    private static ResourceLocation getTextureFromRows(int rows) {
        return switch (rows) {
            case 1 -> CharmResources.GUI_9_TEXTURE;
            case 2 -> CharmResources.GUI_18_TEXTURE;
            default -> throw new IllegalArgumentException("Unsupported row count " + rows);
        };
    }
}
