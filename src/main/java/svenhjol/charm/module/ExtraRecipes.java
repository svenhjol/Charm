package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import javax.annotation.Nullable;
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

    @Config(name = "Grindable horse armor", description = "If true, adds recipes for grinding horse armor.")
    public static boolean useHorseArmor = true;

    public static final Map<Item, Item> horseArmorRecipes = new HashMap<>();

    @Override
    public void init() {
        horseArmorRecipes.put(Items.LEATHER_HORSE_ARMOR, Items.LEATHER);
        horseArmorRecipes.put(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);
        horseArmorRecipes.put(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);
        horseArmorRecipes.put(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);
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
}

