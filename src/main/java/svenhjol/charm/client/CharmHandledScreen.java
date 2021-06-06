package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class CharmHandledScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final ResourceLocation texture;

    public CharmHandledScreen(T screenContainer, Inventory inv, Component titleIn, ResourceLocation texture) {
        super(screenContainer, inv, titleIn);
        this.passEvents = true;
        this.texture = texture;
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
        if (minecraft != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);

            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            this.blit(matrices, x, y, 0, 0, imageWidth, imageHeight);
        }
    }
}
