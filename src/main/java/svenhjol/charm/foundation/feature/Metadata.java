package svenhjol.charm.foundation.feature;


import svenhjol.charm.foundation.Feature;

public final class Metadata {
    public Metadata() {}

    public Class<? extends Feature> feature;

    public String description;

    public boolean enabledByDefault;

    public int priority;

    /**
     * Helper to generate metadata from a feature.
     */
    public static Metadata create(Feature feature) {
        Class<? extends Feature> clazz = feature.getClass();

        if (!clazz.isAnnotationPresent(svenhjol.charm.foundation.annotation.Feature.class)) {
            throw new RuntimeException("Missing feature annotation for " + feature.name());
        }

        var annotation = clazz.getAnnotation(svenhjol.charm.foundation.annotation.Feature.class);

        var metadata = new Metadata();
        metadata.feature = clazz;
        metadata.description = annotation.description();
        metadata.priority = annotation.priority();
        metadata.enabledByDefault = annotation.enabledByDefault();

        return metadata;
    }
}
