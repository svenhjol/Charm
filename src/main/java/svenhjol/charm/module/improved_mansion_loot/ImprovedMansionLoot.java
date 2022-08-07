package svenhjol.charm.module.improved_mansion_loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
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

@CommonModule(mod = Charm.MOD_ID, description = "Mansion loot chests can contain totems, enchanted armor and axes, and treasure enchanted books.")
public class ImprovedMansionLoot extends CharmModule {
    public static final ResourceLocation LOOT_ID = new ResourceLocation(Charm.MOD_ID, "improved_mansion_loot");
    public static LootItemFunctionType LOOT_FUNCTION;

    public static final List<Item> TOOLS = new ArrayList<>();
    public static final List<Item> ARMOR = new ArrayList<>();

    public static int minRolls = 1;
    public static int maxRolls = 3;

    @Override
    public void runWhenEnabled() {
        TOOLS.addAll(Arrays.asList(
            Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_PICKAXE
        ));

        ARMOR.addAll(Arrays.asList(
            Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS
        ));

        LOOT_FUNCTION = CommonRegistry.lootFunctionType(LOOT_ID, new LootItemFunctionType(new ImprovedMansionLootFunction.Serializer()));
        LootTableEvents.MODIFY.register(this::handleLootTables);
    }

    private void handleLootTables(ResourceManager resourceManager, LootTables lootTables, ResourceLocation id, LootTable.Builder supplier, LootTableSource source) {
        if (id.equals(BuiltInLootTables.WOODLAND_MANSION)) {
            var builder = LootPool.lootPool()
                .setRolls(UniformGenerator.between((float)minRolls, (float)maxRolls));

            addItem(builder, Items.IRON_AXE, 10);
            addItem(builder, Items.IRON_CHESTPLATE, 10);
            addItem(builder, Items.BOOK, 5);
            addItem(builder, Items.EMERALD, 4);
            addItem(builder, Items.TOTEM_OF_UNDYING, 2);

            supplier.withPool(builder);
        }
    }

    private void addItem(LootPool.Builder builder, Item item, int weight) {
        builder.add(LootItem.lootTableItem(item).setWeight(weight).apply(() -> new ImprovedMansionLootFunction(new LootItemCondition[0])));
    }
}
