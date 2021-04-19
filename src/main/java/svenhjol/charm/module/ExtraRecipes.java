package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.CheckAnvilRepairCallback;

import javax.annotation.Nullable;
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

    @Config(name = "Leather to repair elytra", description = "If true, leather can be used to repair elytra when insomnia is disabled.")
    public static boolean useLeatherForElytra = true;

    @Config(name = "Grindable horse armor", description = "If true, adds recipes for grinding horse armor.")
    public static boolean useHorseArmor = true;

    public static final Map<Item, Item> horseArmorRecipes = new HashMap<>();

    @Override
    public void init() {
        horseArmorRecipes.put(Items.LEATHER_HORSE_ARMOR, Items.LEATHER);
        horseArmorRecipes.put(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);
        horseArmorRecipes.put(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);
        horseArmorRecipes.put(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);

        CheckAnvilRepairCallback.EVENT.register(this::handleCheckAnvilRepair);
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> removedRecipes = new ArrayList<>();

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
            if (!key) {
                recipes.forEach(recipe -> removedRecipes.add(new Identifier(Charm.MOD_ID, "extra_recipes/" + recipe)));
            }
        });

        return removedRecipes;
    }

    public static boolean tryUpdateGrindstoneOutput(Inventory inputs, Inventory output, @Nullable PlayerEntity player) {
        if (!isExtraRecipesEnabled())
            return false;

        Item slot0 = inputs.getStack(0).getItem();
        Item slot1 = inputs.getStack(1).getItem();

        if (horseArmorRecipes.containsKey(slot0)) {
            output.setStack(0, new ItemStack(horseArmorRecipes.get(slot0)));
        } else if (horseArmorRecipes.containsKey(slot1)) {
            output.setStack(0, new ItemStack(horseArmorRecipes.get(slot1)));
        }
        return true;
    }

    private static boolean isExtraRecipesEnabled() {
        return ModuleHandler.enabled(ExtraRecipes.class);
    }

    private boolean handleCheckAnvilRepair(AnvilScreenHandler handler, PlayerEntity player, ItemStack leftStack, ItemStack rightStack) {
        if (!useLeatherForElytra || player == null || player.world == null)
            return false;

        // don't activate if insomnia is enabled
        if (!player.world.isClient && player.world.getGameRules().getBoolean(GameRules.DO_INSOMNIA))
            return false;

        return leftStack.getItem() == Items.ELYTRA && rightStack.getItem() == Items.LEATHER;
    }
}

