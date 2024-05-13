package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Resolve;

public interface SubFeature<P extends Feature> {
    default P parent() {
        return Resolve.feature(typeForParent());
    }

    Class<P> typeForParent();
}
