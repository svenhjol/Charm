package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID)
public class ColoredGlintSmithingTemplatesClient extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();
        registry.itemTab(
            ColoredGlintSmithingTemplates.item,
            CreativeModeTabs.INGREDIENTS,
            Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE
        );
    }
}
