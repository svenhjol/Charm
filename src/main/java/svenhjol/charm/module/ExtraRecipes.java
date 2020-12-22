package svenhjol.charm.module;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Adds custom recipes.")
public class ExtraRecipes extends CharmModule {
    @Config(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean useGildedBlackstone = true;

    @Config(name = "Trident", description = "If true, adds a recipe for the Trident using prismarine shards and crystals.")
    public static boolean useTrident = true;

    @Config(name = "Cyan Dye", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    public static boolean useCyanDye = true;

    @Config(name = "Green Dye", description = "If true, adds a recipe for Green Dye using vines.")
    public static boolean useGreenDye = true;
    @Override


    public List<Identifier> getRecipesToRemove() {
        List<Identifier> removedRecipes = new ArrayList<>();
        if (!useGildedBlackstone)
            removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/gilded_blackstone"));
        if (!useTrident)
            removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/trident"));
        if (!useCyanDye)
            removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/cyan_dye"));
        if (!useGreenDye)
            removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/green_dye"));
        return removedRecipes;
    }
}

