package svenhjol.charm.charmony.feature;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;

public interface ChildFeature<P extends Feature> {
    default P parent() {
        return Resolve.feature(typeForParent());
    }

    Class<P> typeForParent();
}
