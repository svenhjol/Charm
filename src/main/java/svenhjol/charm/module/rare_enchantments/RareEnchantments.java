package svenhjol.charm.module.rare_enchantments;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.QuickChargeEnchantment;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.UpdateAnvilCallback;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.colored_glints.ColoredGlints;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;
import java.util.function.Consumer;

@CommonModule(mod = Charm.MOD_ID, description = "Rare enchantments are enchanted books with one level above the enchantment's maximum level.\n" +
    "Items with rare enchantments have a unique enchantment glow.")
public class RareEnchantments extends CharmModule {
    public static final String RARE_ENCHANTMENT_TAG = "charm_rare_enchantment";
    public static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();
    public static final ResourceLocation LOOT_ID = new ResourceLocation(Charm.MOD_ID, "rare_enchantments_loot");
    public static LootItemFunctionType LOOT_FUNCTION;
    public static final Map<ResourceLocation, Float> LOOT_TABLES = new HashMap<>();

    @Config(name = "Stronghold chest chance", description = "Chance (out of 1.0) of a rare enchantment being found in a stronghold library chest.")
    public static float strongholdChance = 1.0F;

    @Config(name = "Ancient city chest chance", description = "Chance (out of 1.0) of a rare enchantment being found in an ancient city chest.")
    public static float ancientCityChance = 0.5F;

    @Config(name = "Minimum books per chest", description = "Minimum number of rare enchanted books that will be in the chest.")
    public static int minRolls = 1;

    @Config(name = "Maximum books per chest", description = "Maximum number of rare enchanted books that will be in the chest.")
    public static int maxRolls = 2;

    @Config(name = "Number of extra levels", description = "Number of levels above the maximum enchantment level. All enchantments are capped at 10.")
    public static int extraLevels = 1;

    @Config(name = "Apply cost", description = "Cost (in levels) to apply the rare enchanted book.")
    public static int applyCost = 30;

    @Config(name = "Change color cost", description = "Cost (in levels) to apply a different enchantment color with a dye.")
    public static int changeColorCost = 5;

    @Config(name = "Valid enchantments", description = "List of enchantments that can be increased by one level.")
    public static List<String> enchantments = Arrays.asList(
        "minecraft:protection",
        "minecraft:fire_protection",
        "minecraft:feather_falling",
        "minecraft:blast_protection",
        "minecraft:respiration",
        "minecraft:thorns",
        "minecraft:depth_strider",
        "minecraft:frost_walker",
        "minecraft:soul_speed",
        "minecraft:swift_sneak",
        "minecraft:sharpness",
        "minecraft:smite",
        "minecraft:bane_of_arthropods",
        "minecraft:knockback",
        "minecraft:fire_aspect",
        "minecraft:looting",
        "minecraft:sweeping",
        "minecraft:efficiency",
        "minecraft:unbreaking",
        "minecraft:fortune",
        "minecraft:power",
        "minecraft:punch",
        "minecraft:luck_of_the_sea",
        "minecraft:loyalty",
        "minecraft:impaling",
        "minecraft:riptide",
        "minecraft:quick_charge",
        "minecraft:piercing"
    );

    @Override
    public void register() {
        for (String enchantment : enchantments) {
            Registry.ENCHANTMENT.getOptional(new ResourceLocation(enchantment)).ifPresent(ENCHANTMENTS::add);
        }

        LOOT_FUNCTION = CommonRegistry.lootFunctionType(LOOT_ID, new LootItemFunctionType(new RareEnchantmentLootFunction.Serializer()));

        extraLevels = Math.max(1, extraLevels); // Don't allow levels below zero.
    }

    @Override
    public void runWhenEnabled() {
        LootTableEvents.MODIFY.register(this::handleLootTables);
        UpdateAnvilCallback.EVENT.register(this::handleUpdateAnvil);

        registerLootTable(BuiltInLootTables.STRONGHOLD_LIBRARY, strongholdChance);
        registerLootTable(BuiltInLootTables.ANCIENT_CITY, ancientCityChance);
    }

    public static void applyTag(ItemStack stack) {
        stack.getOrCreateTag().putBoolean(RARE_ENCHANTMENT_TAG, true);
    }

    public static boolean hasTag(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null) {
            return tag.getBoolean(RARE_ENCHANTMENT_TAG);
        }
        return false;
    }

    public static boolean checkValidEnchantment(Enchantment enchantment0, Enchantment enchantment1, ItemStack slot0, ItemStack slot1) {
        return enchantment1.canEnchant(slot0) && hasTag(slot1);
    }

    public static boolean preserveHighestLevelEnchantment(Map<Enchantment, Integer> enchantments, ItemStack slot0, ItemStack slot1, ItemStack output) {
        if (slot1.isEmpty() || output.isEmpty()) return false;

        Map<Enchantment, Integer> reset = new HashMap<>();
        var enchantments1 = EnchantmentHelper.getEnchantments(slot1);

        enchantments1.forEach((e, l) -> {
            if (l > e.getMaxLevel()) {
                reset.put(e, getMaxLevelCap(e, l));
            }
        });

        reset.forEach((e, l) -> {
            if (enchantments.containsKey(e)) {
                enchantments.put(e, l);
            }
        });

        EnchantmentHelper.setEnchantments(enchantments, output);

        if (hasTag(slot0) || hasTag(slot1)) {
            applyTag(output);

            // Also try apply color.
            if (Charm.LOADER.isEnabled(ColoredGlints.class)) {
                String color;
                if (ColoredGlints.hasColoredGlint(slot0)) {
                    color = ColoredGlints.getColoredGlint(slot0);
                } else {
                    color = ColoredGlints.getColoredGlint(slot1);
                }
                var dyeColor = DyeColor.byName(color, ColoredGlints.getDefaultGlintColor());
                ColoredGlints.applyGlint(output, dyeColor);
            }
        }

        return true;
    }

    public static void registerLootTable(ResourceLocation id, float chance) {
        LOOT_TABLES.put(id, chance);
    }

    private void handleLootTables(ResourceManager resourceManager, LootTables lootTables, ResourceLocation id, LootTable.Builder builder, LootTableSource source) {
        if (LOOT_TABLES.containsKey(id)) {
            var chance = LOOT_TABLES.get(id);
            var lootPool = LootPool.lootPool()
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .setRolls(UniformGenerator.between((float)minRolls, (float)maxRolls))
                .add(LootItem.lootTableItem(Items.BOOK)
                    .setWeight(1)
                    .apply(() -> new RareEnchantmentLootFunction(new LootItemCondition[0])));

            builder.withPool(lootPool);
        }
    }

    private InteractionResult handleUpdateAnvil(AnvilMenu menu, Player player, ItemStack left, ItemStack right, int baseCost, Consumer<ItemStack> setOutput, Consumer<Integer> setXpCost, Consumer<Integer> setMaterialCost) {
        if (hasTag(left) && right.getItem() instanceof DyeItem dye) {
            var out = left.copy();
            ColoredGlints.applyGlint(out, dye.getDyeColor());

            setXpCost.accept(changeColorCost);
            setMaterialCost.accept(1);
            setOutput.accept(out);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static int getMaxLevelCap(Enchantment enchantment, int level) {
        if (enchantment instanceof QuickChargeEnchantment) {
            return Math.min(5, level);
        } else if (enchantment instanceof ThornsEnchantment) {
            return Math.min(7, level);
        }

        return level;
    }
}
