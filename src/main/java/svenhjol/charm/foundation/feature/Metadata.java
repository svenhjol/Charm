package svenhjol.charm.foundation.feature;


import svenhjol.charm.foundation.Feature;

public final class Metadata<T extends Feature> {
    public Class<? extends T> feature;
    public String description;
    public boolean canBeDisabled;
    public boolean enabledByDefault;
    public int priority;

    public Metadata(Class<? extends T> clazz) {
        this.feature = clazz;
    }
}
