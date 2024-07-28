package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import svenhjol.charm.Charm;

public abstract class SettingsScreen extends Screen {
    public static final int CONTENT_TOP_MARGIN = 6;
    public static final int CONTENT_BOTTOM_MARGIN = 10;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    protected boolean requiresRestart = false;

    protected SettingsScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();
        addTitle();
        addContents();
        addFooter();

        layout().visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout().arrangeElements();
    }

    public void requiresRestart() {
        requiresRestart = true;
    }

    public HeaderAndFooterLayout layout() {
        return layout;
    }

    public void addChildren(AbstractWidget... widgets) {
        for (var widget : widgets) {
            this.addWidget(widget);
        }
    }

    protected void addTitle() {
        layout().addTitleHeader(title, font);
    }

    protected abstract void addContents();

    protected abstract void addFooter();

    protected static WidgetSprites makeButton(String name) {
        return new WidgetSprites(
            Charm.id("widget/settings/" + name + "_button"),
            Charm.id("widget/settings/" + name + "_button_disabled"),
            Charm.id("widget/settings/" + name + "_button_highlighted"));
    }
}
