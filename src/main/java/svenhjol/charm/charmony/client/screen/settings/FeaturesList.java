package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
        addEntry(new Entry(feature));
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Feature feature;
        private final ImageButton enableButton;
        private final ImageButton disableButton;
        private final ImageButton configureButton;
        private final Tooltip enableButtonTooltip;
        private final Tooltip disableButtonTooltip;
        private final Tooltip configureButtonTooltip;

        public Entry(Feature feature) {
            this.feature = feature;

            this.enableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.ENABLE_BUTTON, button -> enable());

            this.disableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.DISABLE_BUTTON, button -> disable());

            this.configureButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.CONFIG_BUTTON, button -> configure());

            this.enableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.enable_feature", feature.name()));
            this.disableButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.disable_feature", feature.name()));
            this.configureButtonTooltip = Tooltip.create(Component.translatable("gui.charm.settings.configure_feature", feature.name()));

            refreshState();
            FeaturesList.this.parent.addChildren(enableButton, disableButton, configureButton);
        }

        private void refreshState() {
            // Set default state.
            configureButton.visible = true;
            configureButton.active = false;
            disableButton.visible = false;
            disableButton.active = false;
            enableButton.visible = false;
            enableButton.active = false;

            if (!feature.canBeDisabled()) {
                disableButton.visible = true;
                disableButton.active = false;
                enableButton.active = false;
            } else if (feature.isEnabled()) {
                disableButton.visible = true;
                disableButton.active = true;
            } else if (feature instanceof ChildFeature<?> child && !child.parent().isEnabled()) {
                enableButton.visible = true;
                enableButton.active = false;
            } else {
                enableButton.visible = true;
                enableButton.active = true;
            }

            if (feature.isEnabled() && ConfigHelper.featureHasConfig(feature))  {
                configureButton.active = true;
            }
        }

        private void setStateAndUpdate(boolean state) {
            feature.setEnabled(state);
            feature.setEnabledInConfig(state);
            writeConfig();
            refreshState();
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
