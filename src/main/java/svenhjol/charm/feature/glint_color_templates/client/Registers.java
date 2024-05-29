package svenhjol.charm.feature.glint_color_templates.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplatesClient;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<GlintColorTemplatesClient> {
    public Registers(GlintColorTemplatesClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.item,
            CreativeModeTabs.INGREDIENTS,
            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
        );
    }
}
