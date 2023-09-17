package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

@ClientFeature(mod = Charm.MOD_ID, canBeDisabled = false)
public class ColoredGlintSmithingTemplatesClient extends CharmFeature {
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
