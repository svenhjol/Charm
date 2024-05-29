package svenhjol.charm.charmony.feature;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;

public interface FeatureResolver<F extends Feature> {
    /**
     * A resolved reference to the feature class.
     * @return Feature
     */
    default F feature() {
        return Resolve.feature(typeForFeature());
    }

    /**
     * The feature class type.
     * @return Class type to provide IDE completion.
     */
    Class<F> typeForFeature();
}
