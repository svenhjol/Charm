package svenhjol.charm.module.bookcases;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BookcaseScreen extends AbstractContainerScreen<AbstractContainerMenu> {
    public BookcaseScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.passEvents = true;
        this.imageWidth = 175;
        this.imageHeight = 168;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        font.draw(poseStack, title.getVisualOrderText(), 8.0F, 6.0F, 4210752);
        font.draw(poseStack, playerInventoryTitle.getVisualOrderText(), 8.0F, (float)imageHeight - 94, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (minecraft != null) {
            RenderSystem.setShaderTexture(0, BookcasesClient.GUI_TEXTURE);

            var x = (width - imageWidth) / 2;
            var y = (height - imageHeight) / 2;
            blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        }
    }
}
