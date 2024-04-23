package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.diagnostics.Diagnostics;
import svenhjol.charm.feature.recipes.Recipes;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class Charm {
    public static final String ID = "charm";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }

    public static List<Class<? extends CommonFeature>> features() {
        return List.of(
            Advancements.class,
            ColoredGlints.class,
            Core.class,
            Diagnostics.class,
            Recipes.class,
            SmoothGlowstone.class
        );
    }
}
