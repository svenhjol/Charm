package svenhjol.charm.module;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Adds a recipe for Gilded Blackstone.")
public class ExtraRecipes extends CharmModule {
    @Config(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean useGildedBlackstone = true;

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> removedRecipes = new ArrayList<>();

        if (!useGildedBlackstone)
            removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/gilded_blackstone"));

        return removedRecipes;
    }
}
