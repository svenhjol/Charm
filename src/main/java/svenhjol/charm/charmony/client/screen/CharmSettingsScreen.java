package svenhjol.charm.charmony.client.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CharmSettingsScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.charm.settings");
    private final Screen parent;
    private final String modId;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final List<Feature> cachedFeatures = new LinkedList<>();

    private SettingsList settings;

    public CharmSettingsScreen(String modId, Screen parent) {
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

    public HeaderAndFooterLayout layout() {
        return layout;
    }

    protected void addTitle() {
        layout.addTitleHeader(title, font);
    }

    protected void addFooter() {
        layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
    }

    protected void addContents() {
        settings = layout.addToContents(new SettingsList(minecraft, width, this));

        for (var feature : features()) {
            settings.addFeature(feature);
        }
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (settings != null) {
            settings.updateSize(width, layout);
        }
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
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
}
