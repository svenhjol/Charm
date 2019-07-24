package svenhjol.charm;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmModLoader;
import svenhjol.charm.base.ClientProxy;
import svenhjol.charm.base.ServerProxy;
import svenhjol.charm.world.CharmWorld;
import svenhjol.charm.tweaks.CharmTweaks;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IProxy;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_NAME = "Charm";
    public static final String MOD_ID = "charm";
    public static final String VERSION = "1.2.3";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Charm()
    {
        Meson.init();

        CharmModLoader.INSTANCE.registerModLoader(MOD_ID).setup(
            new CharmTweaks(),
            new CharmWorld()
        );

        // add the Charm modloader to event bus
//        FMLJavaModLoadingContext.get().getModEventBus().addListener();
    }
}