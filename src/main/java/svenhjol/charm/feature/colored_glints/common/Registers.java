package svenhjol.charm.feature.colored_glints.common;

import net.minecraft.core.component.DataComponentType;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ColoredGlints> {
    public final Supplier<DataComponentType<ColoredGlintData>> data;

    public Registers(ColoredGlints feature) {
        super(feature);

        data = feature.registry().dataComponent("charm_colored_glint",
            () -> builder -> builder
                .persistent(ColoredGlintData.CODEC)
                .networkSynchronized(ColoredGlintData.STREAM_CODEC));
    }
}
