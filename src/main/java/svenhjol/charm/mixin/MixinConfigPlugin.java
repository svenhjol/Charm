package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public class MixinConfigPlugin extends svenhjol.charm.foundation.MixinConfigPlugin {
    @Override
    protected String id() {
        return Charm.ID;
    }

    @Override
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
            feature -> feature.equals("ColoredGlints") && ConfigHelper.isModLoaded("optifabric")
        );
    }
}
