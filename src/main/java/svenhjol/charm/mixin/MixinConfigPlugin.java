package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public final class MixinConfigPlugin extends svenhjol.charm.foundation.MixinConfigPlugin {
    @Override
    protected String id() {
        return Charm.ID;
    }

    @Override
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
            feature -> feature.equals("GlintColoring") && ConfigHelper.isModLoaded("optifabric"),
            feature -> feature.equals("GrindstoneDisenchanting") && ConfigHelper.isModLoaded("grindenchantments")
        );
    }
}
