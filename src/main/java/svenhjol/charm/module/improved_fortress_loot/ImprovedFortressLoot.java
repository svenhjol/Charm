package svenhjol.charm.module.improved_fortress_loot;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Nether fortress loot chests can contain enchanted armor and weapons, more diamonds, treasure enchanted books, blaze powder, and (rarely) wither skeleton skulls.")
public class ImprovedFortressLoot extends CharmModule {
    public static final ResourceLocation LOOT_ID = new ResourceLocation(Charm.MOD_ID, "improved_fortress_loot");
    public static LootItemFunctionType LOOT_FUNCTION;

    public static final List<Item> TOOLS = new ArrayList<>();
    public static final List<Item> ARMOR = new ArrayList<>();

    public static int minRolls = 1;
    public static int maxRolls = 3;

    @Override
    public void runWhenEnabled() {
        TOOLS.addAll(Arrays.asList(
            Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE,
            Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_PICKAXE
        ));

        ARMOR.addAll(Arrays.asList(
            Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
            Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS
        ));

        LOOT_FUNCTION = CommonRegistry.lootFunctionType(LOOT_ID, new LootItemFunctionType(new ImprovedFortressLootFunction.Serializer()));
        LootTableLoadingCallback.EVENT.register(this::handleLootTables);
    }

    private void handleLootTables(ResourceManager manager, LootTables tables, ResourceLocation id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter) {
        if (id.equals(BuiltInLootTables.NETHER_BRIDGE)) {
            var builder = FabricLootPoolBuilder.builder()
                .rolls(UniformGenerator.between((float)minRolls, (float)maxRolls));

            addItem(builder, Items.GOLDEN_SWORD, 10);
            addItem(builder, Items.GOLDEN_CHESTPLATE, 10);
            addItem(builder, Items.DIAMOND, 5);
            addItem(builder, Items.BOOK, 5);
            addItem(builder, Items.BLAZE_POWDER, 3);
            addItem(builder, Items.WITHER_SKELETON_SKULL, 1);

            supplier.withPool(builder);
        }
    }

    private void addItem(FabricLootPoolBuilder builder, Item item, int weight) {
        builder.with(LootItem.lootTableItem(item).setWeight(weight).apply(() -> new ImprovedFortressLootFunction(new LootItemCondition[0])));
    }
}
