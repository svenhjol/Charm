package svenhjol.charm.charmony.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;

public class FeatureConfigScreen extends Screen {
    private final Feature feature;
    private final CharmSettingsScreen parent;

    public FeatureConfigScreen(Feature feature, CharmSettingsScreen parent) {
        super(Component.translatable("gui.charm.settings.feature.title", feature.name()));
        this.feature = feature;
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.render(guiGraphics, mouseX, mouseY, tickDelta);

        if (minecraft == null) {
            return;
        }

        int midX = width / 2;
        var font = minecraft.font;

        guiGraphics.drawCenteredString(font, title, midX, 30, 0xffffff);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }
}
