package svenhjol.charm.module;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, description = "Adds custom recipes.")
public class ExtraRecipes extends CharmModule {
    @Config(name = "Ore block from raw ore block", description = "If true, adds a blast furnace recipe for smelting raw ore blocks into ore blocks.")
    public static boolean useRawOreBlocks = true;

    @Config(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean useGildedBlackstone = true;

    @Config(name = "Trident", description = "If true, adds a recipe for the Trident using prismarine shards and crystals.")
    public static boolean useTrident = true;

    @Config(name = "Cyan Dye from warped roots", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    public static boolean useCyanDye = true;

    @Config(name = "Green Dye from yellow and blue", description = "If true, adds a recipe for Green Dye using yellow and blue dyes.")
    public static boolean useGreenDye = true;

    @Config(name = "Simpler Soul Torch", description = "If true, adds a recipe for Soul Torches using soul sand/soul soil and sticks.")
    public static boolean useSoulTorch = true;

    @Config(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean useBread = true;

    @Config(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean usePaper = true;

    @Config(name = "Bundle from leather", description = "If true, adds a recipe for crafting bundles from leather.")
    public static boolean useBundle = true;

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> removedRecipes = new ArrayList<>();

        Map<Boolean, List<String>> useRecipes = new HashMap<>();
        useRecipes.put(useRawOreBlocks, Arrays.asList(
            "copper_block_from_blasting_raw_copper_block.json",
            "gold_block_from_blasting_raw_gold_block",
            "iron_block_from_blasting_raw_iron_block"
        ));
        useRecipes.put(useGildedBlackstone, Collections.singletonList("gilded_blackstone"));
        useRecipes.put(useTrident, Collections.singletonList("trident"));
        useRecipes.put(useCyanDye, Collections.singletonList("cyan_dye"));
        useRecipes.put(useGreenDye, Collections.singletonList("green_dye"));
        useRecipes.put(useSoulTorch, Collections.singletonList("soul_torch"));
        useRecipes.put(useBread, Collections.singletonList("bread"));
        useRecipes.put(usePaper, Collections.singletonList("paper"));
        useRecipes.put(useBundle, Collections.singletonList("bundle"));

        useRecipes.forEach((key, recipes) -> {
            if (!key) {
                recipes.forEach(recipe -> removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/" + recipe)));
            }
        });

        return removedRecipes;
    }
}

