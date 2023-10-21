package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyMixinConfigPlugin;

public class MixinConfigPlugin extends CharmonyMixinConfigPlugin {
    @Override
    protected String getModId() {
        return Charm.ID;
    }
}
