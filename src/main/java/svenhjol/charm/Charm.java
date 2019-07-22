package svenhjol.charm;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.charm.base.CharmModLoader;
import svenhjol.charm.base.ClientProxy;
import svenhjol.charm.base.ServerProxy;
import svenhjol.charm.tweaks.CharmTweaks;
import svenhjol.meson.iface.IProxy;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_NAME = "Charm";
    public static final String MOD_ID = "charm";
    public static final String MOD_VERSION = "@MOD_VERSION@";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Charm()
    {
        CharmModLoader.INSTANCE.add(
            new CharmTweaks()
        );

        // add the Charm modloader to event bus
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CharmModLoader.INSTANCE::setup);
    }
}