package svenhjol.charm;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.ClientProxy;
import svenhjol.charm.base.CommonProxy;
import svenhjol.meson.iface.IMesonSidedProxy;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_ID = "charm";
    public static IMesonSidedProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public Charm()
    {
        proxy.init();
    }
}