package svenhjol.charm.feature.colored_glint_smithing_templates;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class ColoredGlintSmithingTemplatesClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ColoredGlintSmithingTemplates.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
