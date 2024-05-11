package svenhjol.charm.foundation.feature;


import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.enums.Side;

public final class Metadata<T extends Feature> {
    public Class<? extends T> feature;

    public String description;

    public boolean enabledByDefault;

    public int priority;

    public Side side;

    public Metadata(Class<? extends T> clazz) {
        this.feature = clazz;
    }
}
