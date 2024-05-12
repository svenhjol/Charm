package svenhjol.charm.feature.colored_glint_templates;

import svenhjol.charm.feature.colored_glint_templates.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class ColoredGlintTemplatesClient extends ClientFeature {
    public final ColoredGlintTemplates common;
    public final Registers registers;

    public ColoredGlintTemplatesClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ColoredGlintTemplates.class);
        registers = new Registers(this);
    }
}
