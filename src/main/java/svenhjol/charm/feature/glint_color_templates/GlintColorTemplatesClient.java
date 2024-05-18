package svenhjol.charm.feature.glint_color_templates;

import svenhjol.charm.feature.glint_color_templates.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class GlintColorTemplatesClient extends ClientFeature implements CommonResolver<GlintColorTemplates> {
    public final Registers registers;

    public GlintColorTemplatesClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<GlintColorTemplates> typeForCommon() {
        return GlintColorTemplates.class;
    }
}
