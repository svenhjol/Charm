package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.annotation.Configurable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class FeatureConfigList extends AbstractSelectionList<FeatureConfigList.Entry> {
    private static final Log LOGGER = new Log("FeatureConfigList");
    private final Feature feature;
    private boolean requiresRestart = false;

    public FeatureConfigList(Feature feature, Minecraft minecraft, int width, FeatureConfigScreen parent) {
        super(minecraft, width, parent.layout().getContentHeight(), parent.layout().getHeaderHeight(), 25);
        this.feature = feature;

        var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
        for (var field : fields) {
            try {
                var config = field.getDeclaredAnnotation(Configurable.class);
                if (config == null) continue;

                field.setAccessible(true);
                var fieldName = config.name();
                var fieldValue = field.get(null);
                if (fieldValue instanceof Boolean) {
                    addEntry(new BooleanEntry(field, Component.literal(fieldName)));
                }

            } catch (Exception e) {
                LOGGER.error("Could not initialize field " + field + ": " + e.getMessage());
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // no op
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    public void writeConfig() {
        var loader = feature.loader();
        var features = loader.features();

        loader.config().ifPresent(config -> config.writeConfig(features));
    }

    public boolean requiresRestart() {
        return requiresRestart;
    }

    public class BooleanEntry extends Entry {
        private final Button enabledButton = Button.builder(
            Component.translatable("gui.charm.settings.enabled"), b -> disable()).width(100).build();
        private final Button disabledButton = Button.builder(
            Component.translatable("gui.charm.settings.disabled"), b -> enable()).width(100).build();

        public BooleanEntry(Field field, Component label) {
            super(field, label);
            refreshState();
        }

        private void refreshState() {
            if (val instanceof Boolean b) {
                this.enabledButton.visible = b;
                this.disabledButton.visible = !b;
            } else {
                this.enabledButton.visible = true;
                this.enabledButton.active = false;
                this.disabledButton.visible = false;
            }
        }

        public void enable() {
            updateValue(true, this::refreshState);
        }

        public void disable() {
            updateValue(false, this::refreshState);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int click) {
            if (handleButtonClick(mouseX, mouseY, click, enabledButton, disabledButton)) {
                return false;
            }
            return super.mouseClicked(mouseX, mouseY, click);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int y, int offsetX, int mouseX, int mouseY, float tickDelta) {
            var buttonX = FeatureConfigList.this.getScrollbarPosition() - enabledButton.getWidth() - 10;
            var buttonY = y - 2;

            enabledButton.setPosition(buttonX, buttonY);
            enabledButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            disabledButton.setPosition(buttonX, buttonY);
            disabledButton.render(guiGraphics, mouseX, mouseY, tickDelta);
        }
    }

    public abstract class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Field field;
        private final Component label;

        protected Object val;

        public Entry(Field field, Component label) {
            this.field = field;
            this.label = label;

            try {
                val = field.get(null);
            } catch (Exception e) {
                LOGGER.error("Could not get field value for " + field + ": " + e.getMessage());
            }
        }

        protected void updateValue(Object val, Runnable onSuccess) {
            try {
                field.set(null, val);
                this.val = val;
                var config = field.getDeclaredAnnotation(Configurable.class);
                FeatureConfigList.this.requiresRestart = config.requireRestart();
                writeConfig();
                onSuccess.run();
            } catch (Exception e) {
                LOGGER.error("Could not set field value for " + field + ": " + e.getMessage());
            }
        }

        public abstract void render(GuiGraphics guiGraphics, int y, int offsetX, int mouseX, int mouseY, float tickDelta);

        protected boolean handleButtonClick(double mouseX, double mouseY, int click, Button... buttons) {
            for (Button button : buttons) {
                if (button.isMouseOver(mouseX, mouseY)) {
                    button.mouseClicked(mouseX, mouseY, click);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            var font = FeatureConfigList.this.minecraft.font;
            var color = 0xffe0c0;
            guiGraphics.drawString(font, label, offsetX + 5, y + 2, color);
            render(guiGraphics, y, offsetX, mouseX, mouseY, tickDelta);
        }
    }
}
