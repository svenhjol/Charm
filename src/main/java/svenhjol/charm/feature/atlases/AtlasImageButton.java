package svenhjol.charm.feature.atlases;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;

import java.util.function.Supplier;

public class AtlasImageButton extends ImageButton {
    private final Supplier<Integer> x;
    private final Supplier<Integer> y;
    private final WidgetSprites sprite;

    public AtlasImageButton(Supplier<Integer> x, Supplier<Integer> y, int width, int height,
                            WidgetSprites sprite, OnPress onPress) {
        super(x.get(), y.get(), width, height, sprite, onPress);
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        setX(x.get());
        setY(y.get());
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        setX(x.get());
        setY(y.get());
        return super.mouseClicked(mouseX, mouseY, button);
    }

//    public void renderButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float mouseDelta) {
//        int yTex = yTexStart;
//
//        if (!active) {
//            yTex += yDiffDisabled;
//        } else if (isHoveredOrFocused()) {
//            yTex += yDiffHighlight;
//        }
//
//        RenderSystem.enableDepthTest();
//        guiGraphics.blit(resourceLocation, getX(), getY(), xTexStart, yTex, width, height, 256, 256);
//    }
}
