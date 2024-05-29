package svenhjol.charm.charmony.feature;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;

public interface LinkedFeature<LF extends Feature> {
    /**
     * A resolved reference to a linked feature class.
     * @return Linked feature
     */
    default LF linked() {
        return Resolve.feature(typeForLinked());
    }

    /**
     * The linked feature class type.
     * @return Class type to provide IDE completion.
     */
    Class<LF> typeForLinked();
}
