package svenhjol.charm.module.extra_boats;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, priority = 5, description = "Adds crimson and warped wood boats.")
public class ExtraBoats extends CharmModule {
    public static final String CRIMSON = VanillaWoodMaterial.CRIMSON.getSerializedName();
    public static final String WARPED = VanillaWoodMaterial.WARPED.getSerializedName();

    @Override
    public void runWhenDisabled() {
        RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "extra_boats/crimson_boat_woodcutting"));
        RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "extra_boats/warped_boat_woodcutting"));
    }
}
