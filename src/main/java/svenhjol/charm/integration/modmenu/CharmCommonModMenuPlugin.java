package svenhjol.charm.integration.modmenu;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyConfig;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.iface.ICommonMod;
import svenhjol.charmony.iface.ILog;

import java.util.List;

public class CharmCommonModMenuPlugin extends BaseModMenuPlugin<ICommonMod, CommonFeature> {
    @Override
    public ICommonMod mod() {
        return Mods.common(Charm.ID);
    }

    @Override
    public ILog log() {
        return mod().log();
    }

    @Override
    public CharmonyConfig config() {
        return (CharmonyConfig)mod().config();
    }

    @Override
    public List<CommonFeature> getFeatures() {
        return mod().loader().getFeatures();
    }
}
