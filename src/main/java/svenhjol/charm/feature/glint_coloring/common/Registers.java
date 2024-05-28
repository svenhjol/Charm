package svenhjol.charm.feature.glint_coloring.common;

import net.minecraft.core.component.DataComponentType;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<GlintColoring> {
    public final Supplier<DataComponentType<GlintColorData>> data;

    public Registers(GlintColoring feature) {
        super(feature);

        data = feature.registry().dataComponent("glint_color",
            () -> builder -> builder
                .persistent(GlintColorData.CODEC)
                .networkSynchronized(GlintColorData.STREAM_CODEC));
    }
}
