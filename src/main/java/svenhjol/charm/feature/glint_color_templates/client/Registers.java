package svenhjol.charm.feature.glint_color_templates.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplatesClient;

public final class Registers extends RegisterHolder<GlintColorTemplatesClient> {
    public Registers(GlintColorTemplatesClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.item.get(),
            CreativeModeTabs.INGREDIENTS,
            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
        );
    }
}
