package svenhjol.charm.charmony.client.screen.settings;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class FeaturesScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.charm.settings.title");

    public static final WidgetSprites CONFIG_BUTTON = makeButton("config");
    public static final WidgetSprites DISABLE_BUTTON = makeButton("disable");
    public static final WidgetSprites ENABLE_BUTTON = makeButton("enable");

    private final Screen parent;
    private final String modId;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final List<Feature> cachedFeatures = new LinkedList<>();

    private boolean requiresRestart;
    private FeaturesList list;

    public FeaturesScreen(String modId, Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.modId = modId;
    }

    @Override
    protected void init() {
        addTitle();
        addContents();
        addFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    public void requiresRestart() {
        this.requiresRestart = true;
    }

    public HeaderAndFooterLayout layout() {
        return layout;
    }

    protected void addTitle() {
        layout.addTitleHeader(title, font);
    }

    protected void addFooter() {
        layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> done()).width(200).build());
    }

    protected void addContents() {
        list = layout.addToContents(new FeaturesList(minecraft, width, this));

        for (var feature : features()) {
            list.addFeature(feature);
        }
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (list != null) {
            list.updateSize(width, layout);
        }
    }

    public void done() {
        if (minecraft == null) return;

        var screen = requiresRestart ? new RestartScreen() : parent;
        minecraft.setScreen(screen);
    }

    protected List<Feature> features() {
        if (cachedFeatures.isEmpty()) {
            List<Feature> features = new LinkedList<>();

            features.addAll(Resolve.features(Side.COMMON, modId));
            features.addAll(Resolve.features(Side.CLIENT, modId));

            features.sort(Comparator.comparing(Feature::name));
            features = features.stream()
                .filter(feature -> (feature.canBeDisabled() || ConfigHelper.featureHasConfig(feature)))
                .toList();

            cachedFeatures.clear();
            cachedFeatures.addAll(features);
        }
        return cachedFeatures;
    }

    private static WidgetSprites makeButton(String name) {
        return new WidgetSprites(
            Charm.id("widget/settings/" + name + "_button"),
            Charm.id("widget/settings/" + name + "_button_disabled"),
            Charm.id("widget/settings/" + name + "_button_highlighted"));
    }
}
