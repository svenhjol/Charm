package svenhjol.charm.module.bookcases;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.init.CharmResources;

public class BookcaseScreen extends AbstractContainerScreen<AbstractContainerMenu> {
    public BookcaseScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.passEvents = true;
        this.imageWidth = 175;
        this.imageHeight = 151;
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
        this.font.draw(matrices, this.playerInventoryTitle.getVisualOrderText(), 8.0F, (float)imageHeight - 94, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (minecraft != null) {
            RenderSystem.setShaderTexture(0, CharmResources.GUI_18_TEXTURE);

            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            blit(matrices, x, y, 0, 0, imageWidth, imageHeight);
        }
    }
}
