package svenhjol.charm;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.automation.CharmAutomation;
import svenhjol.charm.base.CharmLoader;
import svenhjol.charm.base.ClientProxy;
import svenhjol.charm.base.ServerProxy;
import svenhjol.charm.brewing.CharmBrewing;
import svenhjol.charm.crafting.CharmCrafting;
import svenhjol.charm.decoration.CharmDecoration;
import svenhjol.charm.tweaks.CharmTweaks;
import svenhjol.charm.world.CharmWorld;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonSidedProxy;

@Mod(value = Charm.MOD_ID)
public class Charm
{
    public static final String MOD_ID = "charm";
    public static IMesonSidedProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Charm()
    {
        Meson.init();

        CharmLoader.INSTANCE.registerLoader(MOD_ID).setup(
            new CharmAutomation(),
            new CharmBrewing(),
            new CharmCrafting(),
            new CharmDecoration(),
            new CharmTweaks(),
            new CharmWorld()
        );
    }

    public static boolean hasFeature(Class<? extends Feature> feature)
    {
        return CharmLoader.INSTANCE.enabledFeatures.contains(feature);
    }
}