package svenhjol.charm.feature.extra_recipes;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IAdvancementRemoveProvider;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "More ways to craft items using different materials.")
public class ExtraRecipes extends CharmFeature implements IRecipeRemoveProvider, IAdvancementRemoveProvider {
    private static final List<ResourceLocation> INVALID = new ArrayList<>();

    @Configurable(name = "Ore block from raw ore block", description = "If true, adds a blast furnace recipe for smelting raw ore blocks into ore blocks.")
    public static boolean rawOreBlocks = true;

    @Configurable(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean gildedBlackstone = true;

    @Configurable(name = "Cyan Dye from warped roots", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    public static boolean cyanDye = true;

    @Configurable(name = "Green Dye from yellow and blue", description = "If true, adds a recipe for Green Dye using yellow and blue dyes.")
    public static boolean greenDye = true;

    @Configurable(name = "Snowballs from snow blocks", description = "If true, adds a recipe for turning snow blocks back into snowballs.")
    public static boolean snowballs = true;

    @Configurable(name = "Quartz from quartz blocks", description = "If true, adds a recipe for turning quartz blocks back into quartz.")
    public static boolean quartz = true;

    @Configurable(name = "Clay balls from clay blocks", description = "If true, adds a recipe for turning clay blocks back into clay balls.")
    public static boolean clay = true;

    @Configurable(name = "Simpler Soul Torch", description = "If true, adds a recipe for Soul Torches using soul sand/soul soil and sticks.")
    public static boolean soulTorch = true;

    @Configurable(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean bread = true;

    @Configurable(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean paper = true;

    @Configurable(name = "Bundle from leather", description = "If true, adds a recipe for crafting bundles from leather.")
    public static boolean bundle = true;

    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        // Remove recipes that are not valid according to the config.
        List<String> invalid = new ArrayList<>();

        if (!rawOreBlocks) invalid.addAll(Arrays.asList(
            "copper_block_from_blasting_raw_copper_block",
            "gold_block_from_blasting_raw_gold_block",
            "iron_block_from_blasting_raw_iron_block"
        ));
        if (!gildedBlackstone) invalid.add("gilded_blackstone");
        if (!snowballs) invalid.add("snowballs_from_snow_block");
        if (!quartz) invalid.add("quartz_from_quartz_block");
        if (!clay) invalid.add("clay_balls_from_clay_block");
        if (!cyanDye) invalid.add("cyan_dye");
        if (!greenDye) invalid.add("green_dye");
        if (!soulTorch) invalid.add("soul_torch");
        if (!bread) invalid.add("bread");
        if (!paper) invalid.add("paper");
        if (!bundle) invalid.add("bundle");

        for (var recipe : invalid) {
            INVALID.add(Charm.instance().makeId("extra_recipes/" + recipe));
        }
    }

    @Override
    public List<ResourceLocation> getAdvancementsToRemove() {
        return INVALID;
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        return INVALID;
    }
}
