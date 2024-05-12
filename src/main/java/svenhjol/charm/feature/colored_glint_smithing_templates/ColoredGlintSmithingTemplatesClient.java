package svenhjol.charm.feature.colored_glint_smithing_templates;

import svenhjol.charm.feature.colored_glint_smithing_templates.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class ColoredGlintSmithingTemplatesClient extends ClientFeature {
    public final ColoredGlintSmithingTemplates common;
    public final Registers registers;

    public ColoredGlintSmithingTemplatesClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ColoredGlintSmithingTemplates.class);
        registers = new Registers(this);
    }
}
