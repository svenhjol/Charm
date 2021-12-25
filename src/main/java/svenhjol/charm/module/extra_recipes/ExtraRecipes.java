package svenhjol.charm.module.extra_recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.CheckAnvilRepairCallback;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Adds custom recipes.")
public class ExtraRecipes extends CharmModule {
    @Config(name = "Ore block from raw ore block", description = "If true, adds a blast furnace recipe for smelting raw ore blocks into ore blocks.")
    public static boolean rawOreBlocks = true;

    @Config(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean gildedBlackstone = true;

    @Config(name = "Trident", description = "If true, adds a recipe for the Trident using a heart of the sea, prismarine shards and crystals.")
    public static boolean trident = true;

    @Config(name = "Cyan Dye from warped roots", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    public static boolean cyanDye = true;

    @Config(name = "Green Dye from yellow and blue", description = "If true, adds a recipe for Green Dye using yellow and blue dyes.")
    public static boolean greenDye = true;

    @Config(name = "Snowballs from snow blocks", description = "If true, adds a recipe for turning snow blocks back into snowballs.")
    public static boolean snowballs = true;

    @Config(name = "Simpler Soul Torch", description = "If true, adds a recipe for Soul Torches using soul sand/soul soil and sticks.")
    public static boolean soulTorch = true;

    @Config(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean bread = true;

    @Config(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean paper = true;

    @Config(name = "Bundle from leather", description = "If true, adds a recipe for crafting bundles from leather.")
    public static boolean bundle = true;

    @Config(name = "Leather to repair elytra", description = "If true, leather can be used to repair elytra when insomnia is disabled.")
    public static boolean leatherForElytra = true;

    @Override
    public void register() {
        // remove recipes that are not valid according to the config
        List<String> invalid = new ArrayList<>();
        if (!rawOreBlocks) invalid.addAll(Arrays.asList(
            "copper_block_from_blasting_raw_copper_block.json",
            "gold_block_from_blasting_raw_gold_block",
            "iron_block_from_blasting_raw_iron_block"
        ));
        if (!gildedBlackstone) invalid.add("gilded_blackstone");
        if (!snowballs) invalid.add("snowballs_from_snow_block");
        if (!trident) invalid.add("trident");
        if (!cyanDye) invalid.add("cyan_dye");
        if (!greenDye) invalid.add("green_dye");
        if (!soulTorch) invalid.add("soul_torch");
        if (!bread) invalid.add("bread");
        if (!paper) invalid.add("paper");
        if (!bundle) invalid.add("bundle");

        invalid.forEach(recipe -> RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "extra_recipes/" + recipe)));
    }

    @Override
    public void runWhenEnabled() {
        CheckAnvilRepairCallback.EVENT.register(this::handleCheckAnvilRepair);
    }

    private boolean handleCheckAnvilRepair(AnvilMenu handler, Player player, ItemStack leftStack, ItemStack rightStack) {
        if (leftStack.getItem() != Items.ELYTRA || !leatherForElytra || player == null || player.level == null)
            return false; // false to bypass

        // don't activate if insomnia is enabled
        if (!player.level.isClientSide && player.level.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA))
            return false; // false to explicitly deny repair if insomnia is enabled

        return leftStack.getItem() == Items.ELYTRA && rightStack.getItem() == Items.LEATHER;
    }
}

