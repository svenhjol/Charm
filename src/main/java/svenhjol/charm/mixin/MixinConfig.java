package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm.charmony.MixinConfigPlugin;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public final class MixinConfig extends MixinConfigPlugin {
    @Override
    public List<Side> sideChecks() {
        return List.of(Side.CLIENT, Side.COMMON);
    }

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
