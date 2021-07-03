package svenhjol.charm.mixin;

import svenhjol.charm.Charm;

public class CharmMixinConfigPlugin extends BaseMixinConfigPlugin {
    @Override
    public String getModId() {
        return Charm.MOD_ID;
    }
}
