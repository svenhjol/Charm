package svenhjol.charm.mixin;

import svenhjol.charm.CharmClient;
import svenhjol.charm_core.base.CharmMixinConfigPlugin;

public class MixinConfigPlugin extends CharmMixinConfigPlugin {
    @Override
    protected String getModId() {
        return CharmClient.MOD_ID;
    }
}
