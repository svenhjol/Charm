package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.UpdateAnvilCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Adds custom recipes.")
public class ExtraRecipes extends CharmModule {
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
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> removedRecipes = new ArrayList<>();

        Map<Boolean, String> useRecipes = new HashMap<>();
        useRecipes.put(useGildedBlackstone, "gilded_blackstone");
        useRecipes.put(useTrident, "trident");
        useRecipes.put(useCyanDye, "cyan_dye");
        useRecipes.put(useGreenDye, "green_dye");
        useRecipes.put(useSoulTorch, "soul_torch");
        useRecipes.put(useBread, "bread");
        useRecipes.put(usePaper, "paper");
        useRecipes.put(useBundle, "bundle");

        useRecipes.forEach((key, val) -> {
            if (!key) {
                removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/" + val));
            }
        });

        return removedRecipes;
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, PlayerEntity player, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (player == null || player.world == null)
            return ActionResult.PASS;

        // don't activate this recipe if config disabled or insomnia is enabled
        if (!useLeatherForElytra || player.world.getGameRules().getBoolean(GameRules.DO_INSOMNIA))
            return ActionResult.PASS;

        if (left.getItem() != Items.ELYTRA || right.getItem() != Items.LEATHER)
            return ActionResult.PASS;

        int cost = left.getRepairCost();

        out = left.copy();

        // vanilla logic. TODO: move to AnvilHelper
        int o = Math.min(out.getDamage(), out.getMaxDamage() / 4);
        if (o <= 0) {
            apply.accept(ItemStack.EMPTY, 0, 0);
            return ActionResult.PASS;
        }
        out.setDamage(out.getDamage() - o);
        out.setRepairCost(AnvilScreenHandler.getNextCost(cost));

        apply.accept(out, cost, 1);
        return ActionResult.SUCCESS;
    }
}

