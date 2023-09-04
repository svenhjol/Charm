package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmMixinConfigPlugin;

public class MixinConfigPlugin extends CharmMixinConfigPlugin {
    @Override
    protected String getModId() {
        return Charm.MOD_ID;
    }
}
