package svenhjol.charm.charmony.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.helper.ConfigHelper;
import svenhjol.charm.charmony.helper.TextHelper;

import java.util.Optional;

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
        private final ImageButton enableButton;
        private final ImageButton disableButton;
        private final ImageButton configureButton;
        private final Tooltip enableButtonTooltip;
        private final Tooltip disableButtonTooltip;
        private final Tooltip configureButtonTooltip;

        public FeatureEntry(Feature feature, Screen screen) {
            this.screen = screen;
            this.feature = feature;

            this.enableButton = new ImageButton(0, 0, 20, 20,
                CharmSettingsScreen.ENABLE_BUTTON, button -> enable());

            this.disableButton = new ImageButton(0, 0, 20, 20,
                CharmSettingsScreen.DISABLE_BUTTON, button -> disable());

            this.configureButton = new ImageButton(0, 0, 20, 20,
                CharmSettingsScreen.CONFIG_BUTTON, button -> configure());

            // Default button state before feature processing.
            this.configureButton.visible = true;
            this.configureButton.active = false;
            this.disableButton.visible = false;
            this.disableButton.active = false;
            this.enableButton.visible = false;
            this.enableButton.active = false;

            if (!feature.canBeDisabled()) {
                this.disableButton.visible = true;
                this.disableButton.active = false;
            } else if (feature.isEnabled()) {
                this.disableButton.visible = true;
                this.disableButton.active = true;
            } else if (feature instanceof ChildFeature<?> child && !child.parent().isEnabled()) {
                this.enableButton.visible = true;
                this.enableButton.active = false;
            } else {
                this.enableButton.visible = true;
                this.enableButton.active = true;
            }

            if (feature.isEnabled() && ConfigHelper.featureHasConfig(feature))  {
                this.configureButton.active = true;
            }

            enableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.enable_feature", feature.name()));
            disableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.disable_feature", feature.name()));
            configureButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.configure_feature", feature.name()));
        }

        private void enable() {

        }

        private void disable() {

        }

        private void configure() {

        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            int enableX = SettingsList.this.getScrollbarPosition() - enableButton.getWidth() - 10;
            int moreX = enableX - 4 - configureButton.getWidth();
            int buttonY = y - 2;

            var font = SettingsList.this.minecraft.font;
            var color = feature.isEnabled() ? 0xffffff : 0x888888;
            var name = Component.literal(feature.name());
            var descriptionLines = TextHelper.toComponents(feature.description(), 48);
            var nameWidth = font.width(name);
            var textLeft = offsetX + 5;
            var textTop = y + 2;

            // Prepend the feature name to the description lines.
            descriptionLines.addFirst(name.copy().withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GOLD));

            guiGraphics.drawString(font, name, offsetX + 5, y + 2, color);

            if (mouseX >= textLeft && mouseX <= textLeft + nameWidth
                && mouseY >= textTop && mouseY <= textTop + 6) {
                guiGraphics.renderTooltip(font, descriptionLines, Optional.empty(), mouseX, mouseY);
            }

            disableButton.setPosition(enableX, buttonY);
            disableButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            enableButton.setPosition(enableX, buttonY);
            enableButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            configureButton.setPosition(moreX, buttonY);
            configureButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            if (disableButton.active) {
                disableButton.setTooltip(disableButtonTooltip);
            }

            if (enableButton.active) {
                enableButton.setTooltip(enableButtonTooltip);
            }

            if (configureButton.active) {
                configureButton.setTooltip(configureButtonTooltip);
            }
        }
    }
}
