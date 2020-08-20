package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.module.*;
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
            AnvilImprovements.class,
            AutomaticRecipeUnlock.class,
            BatBucket.class,
            CampfiresNoDamage.class,
            ChickensDropFeathers.class,
            Core.class,
            DirtToPath.class,
            FeatherFallingCrops.class,
            HoeHarvesting.class,
            PathToDirt.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            UseTotemFromInventory.class,
            VariantAnimalTextures.class,
            VillagersFollowEmeraldBlocks.class
        ));
    }
}
