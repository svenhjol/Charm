package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm.charmony.MixinConfigPlugin;
import svenhjol.charm.charmony.common.helper.DebugHelper;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public final class MixinConfig extends MixinConfigPlugin {
    @Override
    protected String id() {
        return Charm.ID;
    }

    @Override
    public List<Side> sides() {
        return List.of(Side.CLIENT, Side.COMMON);
    }

    @Override
    public boolean baseNameCheck(String baseName, String mixinClassName) {
        // With compat enabled we don't load ANY mixins EXCEPT accessors.
        if (DebugHelper.isCompatEnabled() && !baseName.equals("accessor")) {
            LOGGER.warn("Compat mode skipping mixin {}", mixinClassName);
            return false;
        }
        return true;
    }

    @Override
    public void consoleOutput(boolean isValid, String mixinClassName) {
        if (DebugHelper.isDebugEnabled()) {
            if (isValid) {
                LOGGER.info("Enabled mixin {}", mixinClassName);
            } else {
                LOGGER.warn("Disabled mixin {}", mixinClassName);
            }
        }
    }

    @Override
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
            feature -> feature.equals("GlintColoring") && ConfigHelper.isModLoaded("optifabric"),
            feature -> feature.equals("GrindstoneDisenchanting") && ConfigHelper.isModLoaded("grindenchantments")
        );
    }
}
