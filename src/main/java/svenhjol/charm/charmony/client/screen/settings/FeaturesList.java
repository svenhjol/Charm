package svenhjol.charm.charmony.client.screen.settings;

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

public class FeaturesList extends AbstractSelectionList<FeaturesList.Entry> {
    private final FeaturesScreen parent;

    public FeaturesList(Minecraft minecraft, int width, FeaturesScreen parent) {
        super(minecraft, width, parent.layout().getContentHeight(), parent.layout().getHeaderHeight(), 25);
        this.parent = parent;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // no op
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    public void addFeature(Feature feature) {
        children().add(new Entry(feature, parent));
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Feature feature;
        private final ImageButton enableButton;
        private final ImageButton disableButton;
        private final ImageButton configureButton;
        private final Tooltip enableButtonTooltip;
        private final Tooltip disableButtonTooltip;
        private final Tooltip configureButtonTooltip;

        public Entry(Feature feature, Screen screen) {
            this.feature = feature;

            this.enableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.ENABLE_BUTTON, button -> enable());

            this.disableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.DISABLE_BUTTON, button -> disable());

            this.configureButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.CONFIG_BUTTON, button -> configure());

            enableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.enable_feature", feature.name()));
            disableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.disable_feature", feature.name()));
            configureButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.configure_feature", feature.name()));

            setButtonState();
        }

        private void setButtonState() {
            // Set default state.
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
        }

        private void setStateAndUpdate(boolean state) {
            feature.setEnabled(state);
            feature.setEnabledInConfig(state);
            writeConfig();
            setButtonState();
            FeaturesList.this.parent.requiresRestart();
        }

        private void enable() {
            setStateAndUpdate(true);
        }

        private void disable() {
            setStateAndUpdate(false);
        }

        private void configure() {
            minecraft.setScreen(new FeatureConfigScreen(feature, parent));
        }

        private void writeConfig() {
            var loader = feature.loader();
            var features = loader.features();

            loader.config().ifPresent(config -> config.writeConfig(features));
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int click) {
            if (enableButton.isMouseOver(mouseX, mouseY)) {
                enableButton.mouseClicked(mouseX, mouseY, click);
                return false;
            }
            if (disableButton.isMouseOver(mouseX, mouseY)) {
                disableButton.mouseClicked(mouseX, mouseY, click);
                return false;
            }
            if (configureButton.isMouseOver(mouseX, mouseY)) {
                configureButton.mouseClicked(mouseX, mouseY, click);
                return false;
            }

            return super.mouseClicked(mouseX, mouseY, click);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            int enableX = FeaturesList.this.getScrollbarPosition() - enableButton.getWidth() - 10;
            int moreX = enableX - 4 - configureButton.getWidth();
            int buttonY = y - 2;

            var font = FeaturesList.this.minecraft.font;
            var color = feature.isEnabled() ? 0xffffff : 0x888888;
            var name = Component.literal(feature.name());
            var descriptionLines = TextHelper.toComponents(feature.description(), 48);
            var nameWidth = font.width(name);
            var textLeft = offsetX + 5;
            var textTop = y + 2;

            // Prepend the feature name to the description lines.
            descriptionLines.addFirst(name.copy()
                .withStyle(ChatFormatting.BOLD)
                .withStyle(feature.isEnabled() ? ChatFormatting.GOLD : ChatFormatting.GRAY));

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
