package svenhjol.charm.integration.modmenu;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyConfig;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.iface.IClientMod;
import svenhjol.charmony.iface.ILog;

import java.util.List;

public class CharmClientModMenuPlugin extends BaseModMenuPlugin<IClientMod, ClientFeature> {
    @Override
    public IClientMod mod() {
        return Mods.client(Charm.ID);
    }

    @Override
    public ILog log() {
        return mod().log();
    }

    @Override
    public CharmonyConfig config() {
        return (CharmonyConfig) mod().config();
    }

    @Override
    public List<ClientFeature> getFeatures() {
        return mod().loader().getFeatures();
    }
}
