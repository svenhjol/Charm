package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;

public class ColoredGlintSmithingTemplatesClient extends ClientFeature {
    @Override
    public void runWhenEnabled() {
        mod().registry().itemTab(
            ColoredGlintSmithingTemplates.item,
            CreativeModeTabs.INGREDIENTS,
            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
        );
    }
}
