package svenhjol.charm.charmony.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.helper.ConfigHelper;

public class SettingsList extends AbstractSelectionList<SettingsList.FeatureEntry> {
    private final Screen parent;

    public SettingsList(Minecraft minecraft, int width, CharmSettingsScreen parent) {
        super(minecraft, width, parent.layout().getContentHeight(), parent.layout().getHeaderHeight(), 25);
        this.parent = parent;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    public void addFeature(Feature feature) {
        children().add(new FeatureEntry(feature, parent));
    }

    public class FeatureEntry extends AbstractSelectionList.Entry<FeatureEntry> {
        private final Screen screen;
        private final Feature feature;
        private final Button enableButton;
        private final Button moreButton;

        public FeatureEntry(Feature feature, Screen screen) {
            this.screen = screen;
            this.feature = feature;

            this.moreButton = Button.builder(Component.literal("More"),
                button -> {}).width(40).build();

            Component enableButtonText;
            Button.OnPress enableButtonAction;
            boolean enableButtonActive;

            if (feature.isEnabled()) {
                enableButtonText = Component.literal("Disable");
                enableButtonAction = button -> {};
                enableButtonActive = true;
            } else if (feature instanceof ChildFeature<?> child && !child.parent().isEnabled()) {
                enableButtonText = Component.literal("Enable");
                enableButtonAction = button -> {};
                enableButtonActive = false;
            } else {
                enableButtonText = Component.literal("Enable");
                enableButtonAction = button -> {};
                enableButtonActive = true;
            }

            this.enableButton = Button.builder(enableButtonText, enableButtonAction)
                .width(50).build();
            this.enableButton.active = enableButtonActive;

            if (!feature.isEnabled() || !ConfigHelper.featureHasConfig(feature))  {
                this.moreButton.active = false;
            }
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            int enableX = SettingsList.this.getScrollbarPosition() - this.enableButton.getWidth() - 10;
            int moreX = enableX - 4 - this.moreButton.getWidth();
            int buttonY = y - 2;

            var font = SettingsList.this.minecraft.font;
            var color = feature.isEnabled() ? 0xffffff : 0x888888;

            guiGraphics.drawString(font, feature.name(), offsetX + 5, y + 2, color);

            this.enableButton.setPosition(enableX, buttonY);
            this.enableButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            this.moreButton.setPosition(moreX, buttonY);
            this.moreButton.render(guiGraphics, mouseX, mouseY, tickDelta);
        }
    }
}
