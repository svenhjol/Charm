package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class CharmImageButton extends ImageButton {
    private final Supplier<Integer> xSupplier;
    private final Supplier<Integer> ySupplier;
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffHighlight;
    private final int yDiffDisabled;

    public CharmImageButton(Supplier<Integer> xSupplier, Supplier<Integer> ySupplier, int width, int height, int xTexStart, int yTexStart, int yDiffHighlight, int yDiffDisabled, ResourceLocation resourceLocation, OnPress onPress) {
        super(xSupplier.get(), ySupplier.get(), width, height, xTexStart, yTexStart, yDiffHighlight, resourceLocation, onPress);
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffHighlight = yDiffHighlight;
        this.yDiffDisabled = yDiffDisabled;

    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        x = xSupplier.get();
        y = ySupplier.get();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        x = xSupplier.get();
        y = ySupplier.get();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float mouseDelta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);
        int yTex = yTexStart;
        if (!active) {
            yTex += yDiffDisabled;
        } else if (isHovered()) {
            yTex += yDiffHighlight;
        }

        RenderSystem.enableDepthTest();
        blit(matrices, x, y, xTexStart, yTex, width, height, 256, 256);
        if (isHovered()) {
            renderToolTip(matrices, mouseX, mouseY);
        }

    }
}
