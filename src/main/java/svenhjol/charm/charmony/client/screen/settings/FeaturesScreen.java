package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class FeaturesScreen extends SettingsScreen {
    private static final Component TITLE = Component.translatable("gui.charm.settings.title");

    public static final WidgetSprites CONFIG_BUTTON = makeButton("config");
    public static final WidgetSprites DISABLE_BUTTON = makeButton("disable");
    public static final WidgetSprites ENABLE_BUTTON = makeButton("enable");

    private final Screen parent;
    private final String modId;

    private FeaturesList list;

    public FeaturesScreen(String modId, Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.modId = modId;
    }

    @Override
    protected void addFooter() {
        layout().addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> done())
            .width(200).build());
    }

    @Override
    protected void addContents() {
        list = layout().addToContents(new FeaturesList(minecraft, modId, width, this));
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        if (list != null) {
            list.updateSize(width, layout());
        }
    }

    /**
     * Updates feature data after being modified by a child screen.
     * Typically this is called by the feature config screen to inform this screen
     * that one or more config items are no longer defaults.
     */
    public void refreshState() {
        list.refreshState();
    }

    public void done() {
        if (minecraft == null) return;

        var screen = requiresRestart ? new RestartScreen() : parent;
        minecraft.setScreen(screen);
    }
}
