package svenhjol.charm.feature.atlases;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class AtlasImageButton extends ImageButton {
    private final Supplier<Integer> xSupplier;
    private final Supplier<Integer> ySupplier;
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffHighlight;
    private final int yDiffDisabled;

    public AtlasImageButton(Supplier<Integer> xSupplier, Supplier<Integer> ySupplier, int width, int height, int xTexStart, int yTexStart, int yDiffHighlight, int yDiffDisabled, ResourceLocation resourceLocation, OnPress onPress) {
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        setX(xSupplier.get());
        setY(ySupplier.get());
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        setX(xSupplier.get());
        setY(ySupplier.get());
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void renderButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float mouseDelta) {
        int yTex = yTexStart;

        if (!active) {
            yTex += yDiffDisabled;
        } else if (isHoveredOrFocused()) {
            yTex += yDiffHighlight;
        }

        RenderSystem.enableDepthTest();
        guiGraphics.blit(resourceLocation, getX(), getY(), xTexStart, yTex, width, height, 256, 256);
    }
}
