package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;

public class FeatureConfigScreen extends Screen {
    private final Feature feature;
    private final FeaturesScreen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    private FeatureConfigList list;

    public FeatureConfigScreen(Feature feature, FeaturesScreen parent) {
        super(Component.translatable("gui.charm.settings.feature.title", feature.name()));
        this.feature = feature;
        this.parent = parent;
    }

    @Override
    protected void init() {
        addTitle();
        addContents();
        addFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    public HeaderAndFooterLayout layout() {
        return layout;
    }

    protected void addTitle() {
        layout.addTitleHeader(title, font);
    }

    protected void addFooter() {
        layout.addToFooter(Button.builder(CommonComponents.GUI_BACK, button -> done()).width(100).build());
    }

    protected void addContents() {
        list = layout.addToContents(new FeatureConfigList(feature, minecraft, width, this));
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (list != null) {
            list.updateSize(width, layout);
        }
    }

    public void done() {
        if (list.requiresRestart()) {
            parent.requiresRestart();
        }

        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }
}
