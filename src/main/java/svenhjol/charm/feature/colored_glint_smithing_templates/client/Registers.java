package svenhjol.charm.feature.colored_glint_smithing_templates.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.colored_glint_smithing_templates.ColoredGlintSmithingTemplatesClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ColoredGlintSmithingTemplatesClient> {
    public Registers(ColoredGlintSmithingTemplatesClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common.registers.item,
            CreativeModeTabs.INGREDIENTS,
            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
        );
    }
}
