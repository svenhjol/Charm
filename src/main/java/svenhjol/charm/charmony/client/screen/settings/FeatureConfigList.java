package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.helper.TextHelper;

import java.lang.reflect.Field;
import java.util.*;

public class FeatureConfigList extends AbstractSelectionList<FeatureConfigList.Entry> {
    private static final Log LOGGER = new Log("FeatureConfigList");
    private final Feature feature;
    private final FeatureConfigScreen parent;
    private final Map<Field, Entry> entries = new HashMap<>();
    private final Map<Field, Object> newValues = new HashMap<>();
    private boolean requiresRestart = false;
    private int extraScrollHeight = 0;

    public FeatureConfigList(Feature feature, Minecraft minecraft, int width, FeatureConfigScreen parent) {
        super(minecraft, width, parent.layout().getContentHeight() - parent.layout().getFooterHeight(), parent.layout().getHeaderHeight(), 25);
        this.feature = feature;
        this.parent = parent;
        this.readConfig();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // no op
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    @Override
    protected boolean isSelectedItem(int i) {
        return false;
    }

    @Override
    protected int getMaxPosition() {
        return super.getMaxPosition() + extraScrollHeight;
    }

    public void readConfig() {
        var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
        for (var field : fields) {
            try {
                var annotation = field.getDeclaredAnnotation(Configurable.class);
                if (annotation == null) continue;

                Entry entry;
                field.setAccessible(true);
                var name = annotation.name();
                var description = annotation.description();
                var fieldValue = field.get(null);

                if (fieldValue instanceof Boolean) {
                    entry = new BooleanEntry(field, name, description);
                } else if (fieldValue instanceof Integer) {
                    entry = new IntegerEntry(field, name, description);
                } else if (fieldValue instanceof Double) {
                    entry = new DoubleEntry(field, name, description);
                } else if (fieldValue instanceof String) {
                    entry = new StringEntry(field, name, description);
                } else if (fieldValue instanceof List<?>) {
                    entry = new StringListEntry(field, name, description);
                } else {
                    entry = null;
                }

                if (entry != null) {
                    entries.put(field, entry);
                    addEntry(entry);
                }

            } catch (Exception e) {
                LOGGER.error("Could not read field " + field + ": " + e.getMessage());
            }
        }
    }

    public void writeConfig() {
        for (var entry : newValues.entrySet()) {
            var field = entry.getKey();
            var val = entry.getValue();

            try {
                field.set(null, val);
            } catch (Exception e) {
                LOGGER.error("Could not set field value for " + field + ": " + e.getMessage());
            }
        }

        var loader = feature.loader();
        var features = loader.features();

        loader.config().ifPresent(config -> config.writeConfig(features));
    }

    public void defaults() {
        newValues.clear();
        for (var entry : entries.values()) {
            entry.resetToDefault();
        }
    }

    public boolean requiresRestart() {
        return requiresRestart;
    }

    public class BooleanEntry extends Entry {
        private final Button enabledButton = Button.builder(
            Component.translatable("gui.charm.settings.true"), b -> disable()).width(60).build();
        private final Button disabledButton = Button.builder(
            Component.translatable("gui.charm.settings.false"), b -> enable()).width(60).build();

        public BooleanEntry(Field field, String label, String description) {
            super(field, label, description);
            addChildren(enabledButton, disabledButton);
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
            updateValue(true);
        }

        public void disable() {
            updateValue(false);
        }

        @Override
        protected void afterUpdate() {
            refreshState();
        }

        @Override
        public void resetToDefault() {
            super.resetToDefault();
            refreshState();
        }

        @Override
        public void renderChild(GuiGraphics guiGraphics, int y, int offsetX, int l, int m, int mouseX, int mouseY, float tickDelta) {
            var buttonX = FeatureConfigList.this.getScrollbarPosition() - enabledButton.getWidth() - 10;
            var buttonY = y - 2;

            enabledButton.setPosition(buttonX, buttonY);
            enabledButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            disabledButton.setPosition(buttonX, buttonY);
            disabledButton.render(guiGraphics, mouseX, mouseY, tickDelta);
        }
    }

    public class IntegerEntry extends SingleTextBoxEntry {
        public IntegerEntry(Field field, String label, String description) {
            super(field, label, description);
        }

        @Override
        protected int maxLength() {
            return 8;
        }

        @Override
        protected void onChange(String newVal) {
            int parsed;
            try {
                parsed = Integer.parseInt(newVal);
            } catch (NumberFormatException e) {
                if (defaultVal == null) {
                    LOGGER.error("Could not get default value for field " + field);
                    return;
                }
                parsed = (int)defaultVal;
            }

            updateValue(parsed);
        }
    }

    public class DoubleEntry extends SingleTextBoxEntry {
        public DoubleEntry(Field field, String label, String description) {
            super(field, label, description);
        }

        @Override
        protected int maxLength() {
            return 16;
        }

        @Override
        protected void onChange(String newVal) {
            double parsed;
            try {
                parsed = Double.parseDouble(newVal);
            } catch (NumberFormatException e) {
                if (defaultVal == null) {
                    LOGGER.error("Could not get default value for field " + field);
                    return;
                }
                parsed = (double)defaultVal;
            }

            updateValue(parsed);
        }
    }

    public class StringEntry extends SingleTextBoxEntry {
        public StringEntry(Field field, String label, String description) {
            super(field, label, description);
        }

        @Override
        protected int maxLength() {
            return 128;
        }

        @Override
        protected int width() {
            return 120;
        }

        @Override
        protected void onChange(String newVal) {
            String parsed;
            try {
                parsed = String.valueOf(newVal);
            } catch (NumberFormatException e) {
                if (defaultVal == null) {
                    LOGGER.error("Could not get default value for field " + field);
                    return;
                }
                parsed = (String)defaultVal;
            }

            updateValue(parsed);
        }
    }

    public class StringListEntry extends MultiLineTextBoxEntry {
        public StringListEntry(Field field, String label, String description) {
            super(field, label, description);
        }

        @Override
        protected int maxLength() {
            return 1000;
        }

        @Override
        protected void onChange(String newVal) {
            List<String> parsed;
            try {
                var asString = String.valueOf(newVal);
                parsed = Arrays.asList(asString.split("\n"));
            } catch (NumberFormatException e) {
                if (defaultVal == null) {
                    LOGGER.error("Could not get default value for field " + field);
                    return;
                }
                parsed = Arrays.asList(((String)defaultVal).split("\n"));
            }

            updateValue(parsed);
        }
    }

    public abstract class SingleTextBoxEntry extends Entry {
        private final EditBox inputBox;

        public SingleTextBoxEntry(Field field, String label, String description) {
            super(field, label, description);
            var font = FeatureConfigList.this.minecraft.font;

            inputBox = new EditBox(font, 0, 0, width() - 1, 15, Component.empty());
            inputBox.setTextColor(-1);
            inputBox.setTextColorUneditable(-1);
            inputBox.setMaxLength(maxLength());
            inputBox.setValue(String.valueOf(val));
            inputBox.setResponder(this::onChange);
            inputBox.visible = false;
            addChildren(inputBox);
        }

        protected int width() {
            return 60;
        }

        protected abstract int maxLength();

        protected abstract void onChange(String newVal);

        @Override
        public void resetToDefault() {
            super.resetToDefault();
            inputBox.setValue(String.valueOf(val));
        }

        @Override
        public void renderChild(GuiGraphics guiGraphics, int y, int offsetX, int l, int m, int mouseX, int mouseY, float tickDelta) {
            var boxX = FeatureConfigList.this.getScrollbarPosition() - inputBox.getWidth() - 10;
            var boxY = y + (m / 2) - 10;
            inputBox.visible = true;
            inputBox.setPosition(boxX, boxY);
            inputBox.renderWidget(guiGraphics, mouseX, mouseY, tickDelta);
        }
    }

    public abstract class MultiLineTextBoxEntry extends Entry {
        private final MultiLineEditBox inputBox;

        public MultiLineTextBoxEntry(Field field, String label, String description) {
            super(field, label, description);
            var font = FeatureConfigList.this.minecraft.font;
            inputBox = new MultiLineEditBox(font, 0, 0, width() - 1, height(), Component.empty(), Component.empty());
            inputBox.setValueListener(this::onChange);
            inputBox.setCharacterLimit(maxLength());
            inputBox.visible = false;
            refreshState();
            addChildren(inputBox);
            addExtraScrollHeight(height() + 20); // Add margin for the text counter
        }

        @SuppressWarnings("unchecked")
        protected void refreshState() {
            var sb = new StringBuilder();
            var lines = (List<String>)val;
            for (String item : lines) {
                sb.append(item).append("\n");
            }
            inputBox.setValue(sb.toString());
        }

        protected int width() {
            return getRowWidth() - 3;
        }

        protected int height() {
            return 60;
        }

        protected abstract int maxLength();

        protected abstract void onChange(String newVal);

        @Override
        public void resetToDefault() {
            super.resetToDefault();
            refreshState();
        }

        @Override
        public void renderChild(GuiGraphics guiGraphics, int y, int offsetX, int l, int m, int mouseX, int mouseY, float tickDelta) {
            var layout = FeatureConfigList.this.parent.layout();
            var boxX = FeatureConfigList.this.getScrollbarPosition() - getRowWidth() - 4;
            var boxY = y + (m / 2) + 10;
            inputBox.visible = true;
            inputBox.setHeight(Mth.clamp(layout.getContentHeight() - y, 0, 60)); // Dumb z-index hack
            inputBox.setPosition(boxX, boxY);
            inputBox.render(guiGraphics, mouseX, mouseY, tickDelta);
        }
    }

    public abstract class Entry extends AbstractSelectionList.Entry<Entry> {
        protected final Field field;
        protected final Configurable annotation;
        protected final String label;
        protected final List<Component> tooltip;

        protected Object defaultVal = null;
        protected Object val;
        protected boolean isUpdating = false;

        public Entry(Field field, String label, String description) {
            this.field = field;
            this.label = label;
            this.tooltip = TextHelper.toComponents(description, 48);
            this.annotation = field.getDeclaredAnnotation(Configurable.class);

            // Try and get the default value for this config item.
            FeatureConfigList.this.feature.loader().config().ifPresent(config ->
                defaultVal = config.defaultValue(field).orElse(null));

            try {
                val = field.get(null);
            } catch (Exception e) {
                LOGGER.error("Could not get field value for " + field + ": " + e.getMessage());
            }

            tooltip.addFirst(Component.literal(label).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GOLD));

            if (defaultVal != null) {
                var out = TextHelper.truncateWithEllipsis(String.valueOf(defaultVal), 32);
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("gui.charm.settings.default_value", out)
                    .withStyle(ChatFormatting.AQUA));
            }

            if (annotation.requireRestart()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("gui.charm.settings.requires_restart")
                    .withStyle(ChatFormatting.RED));
            }
        }

        protected void updateValue(Object val) {
            if (isUpdating) return;

            FeatureConfigList.this.newValues.put(field, val);
            this.val = val;

            if (annotation.requireRestart()) {
                FeatureConfigList.this.requiresRestart = true;
            }

            isUpdating = true;
            afterUpdate();
            isUpdating = false;
        }

        /**
         * Hook called immediately after anything updates the widget.
         * Be careful when using textboxes as input can be ignored/overridden immediately.
         */
        protected void afterUpdate() {
            // hook
        }

        public void resetToDefault() {
            updateValue(defaultVal);
        }

        public void addChildren(AbstractWidget... widgets) {
            FeatureConfigList.this.parent.addChildren(widgets);
        }

        public void addExtraScrollHeight(int height) {
            FeatureConfigList.this.extraScrollHeight += height;
        }

        public abstract void renderChild(GuiGraphics guiGraphics, int y, int offsetX, int l, int m, int mouseX, int mouseY, float tickDelta);

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            var font = FeatureConfigList.this.minecraft.font;
            var color = 0xefefef;
            var isDefaultVal = defaultVal.equals(val);
            var label = Component.literal(this.label);
            var labelWidth = font.width(label);
            var textLeft = offsetX + 5;
            var textTop = y + 2;

            if (!isDefaultVal) {
                label = label.withStyle(ChatFormatting.YELLOW);
            }

            if (mouseX >= textLeft && mouseX <= textLeft + labelWidth
                && mouseY >= textTop && mouseY <= textTop + 6) {
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY + 10);
            }

            guiGraphics.drawString(font, label, offsetX + 5, y + 2, color);
            renderChild(guiGraphics, y, offsetX, l, m, mouseX, mouseY, tickDelta);
        }
    }
}
