package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.module.AutomaticRecipeUnlock;
import svenhjol.charm.module.CampfiresNoDamage;
import svenhjol.charm.module.DirtToPath;
import svenhjol.charm.module.PathToDirt;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Charm extends Meson implements ModInitializer {
    public static final String MOD_ID = "charm";

    @Override
    public void onInitialize() {
        super.init(MOD_ID);
    }

    @Override
    public List<Class<? extends MesonModule>> getModules() {
        return new ArrayList<>(Arrays.asList(
            AutomaticRecipeUnlock.class,
            CampfiresNoDamage.class,
            DirtToPath.class,
            PathToDirt.class
        ));
    }
}
