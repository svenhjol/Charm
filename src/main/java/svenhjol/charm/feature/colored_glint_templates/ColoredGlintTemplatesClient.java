package svenhjol.charm.feature.colored_glint_templates;

import svenhjol.charm.feature.colored_glint_templates.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class ColoredGlintTemplatesClient extends ClientFeature implements CommonResolver<ColoredGlintTemplates> {
    public final Registers registers;

    public ColoredGlintTemplatesClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<ColoredGlintTemplates> typeForCommon() {
        return ColoredGlintTemplates.class;
    }
}
