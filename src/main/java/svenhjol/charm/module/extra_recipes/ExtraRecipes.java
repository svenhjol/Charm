package svenhjol.charm.module.extra_recipes;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.event.CheckAnvilRepairCallback;

import java.util.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;

@Module(mod = Charm.MOD_ID, description = "Adds custom recipes.",
    requiresMixins = {"CheckAnvilRepairCallback"})
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

    @Config(name = "Leather to repair elytra", description = "If true, leather can be used to repair elytra when insomnia is disabled.")
    public static boolean useLeatherForElytra = true;

    @Override
    public void init() {
        CheckAnvilRepairCallback.EVENT.register(this::handleCheckAnvilRepair);
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> removedRecipes = new ArrayList<>();

        Map<Boolean, List<String>> collect = new WeakHashMap<>();
        collect.put(useRawOreBlocks, Arrays.asList(
            "copper_block_from_blasting_raw_copper_block.json",
            "gold_block_from_blasting_raw_gold_block",
            "iron_block_from_blasting_raw_iron_block"
        ));
        collect.put(useGildedBlackstone, Collections.singletonList("gilded_blackstone"));
        collect.put(useTrident, Collections.singletonList("trident"));
        collect.put(useCyanDye, Collections.singletonList("cyan_dye"));
        collect.put(useGreenDye, Collections.singletonList("green_dye"));
        collect.put(useSoulTorch, Collections.singletonList("soul_torch"));
        collect.put(useBread, Collections.singletonList("bread"));
        collect.put(usePaper, Collections.singletonList("paper"));
        collect.put(useBundle, Collections.singletonList("bundle"));

        collect.forEach((key, recipes) -> {
            if (!key)
                recipes.forEach(recipe -> removedRecipes.add(new ResourceLocation(Charm.MOD_ID, "extra_recipes/" + recipe)));
        });

        return removedRecipes;
    }

    private boolean handleCheckAnvilRepair(AnvilMenu handler, Player player, ItemStack leftStack, ItemStack rightStack) {
        if (!useLeatherForElytra || player == null || player.level == null)
            return false;

        // don't activate if insomnia is enabled
        if (!player.level.isClientSide && player.level.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA))
            return false;

        return leftStack.getItem() == Items.ELYTRA && rightStack.getItem() == Items.LEATHER;
    }
}
