package svenhjol.charm.module;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.item.GlintBookItem;
import svenhjol.charm.loot.GlintBookLootFunction;

import java.util.*;

@Module(mod = Charm.MOD_ID, description = "Glint Books that let you change an item's enchantment color. Requires Core 'Enchantment glint override' to be true.")
public class GlintBooks extends CharmModule {
    public static final Identifier LOOT_ID = new Identifier(Charm.MOD_ID, "glint_book_loot");
    public static LootFunctionType LOOT_FUNCTION;
    public static Map<String, GlintBookItem> GLINT_BOOKS = new HashMap<>();

    @Config(name = "XP cost", description = "Number of levels required to add the colored glint to an item.")
    public static int xpCost = 5;

    @Override
    public void register() {
        // register all 16 colored glint books
        for (DyeColor dyeColor : DyeColor.values()) {
            String color = dyeColor.asString();
            GLINT_BOOKS.put(color, new GlintBookItem(this, color));
        }

        // register the loot function to generate glint books in loot tables
        LOOT_FUNCTION = RegistryHandler.lootFunctionType(LOOT_ID, new LootFunctionType(new GlintBookLootFunction.Serializer()));
    }

    @Override
    public boolean depends() {
        return Core.overrideGlint;
    }

    @Override
    public void init() {
        // listen for anvil behavior
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);

        // listen for loot table generation
        LootTableLoadingCallback.EVENT.register(this::handleLootTables);
    }

    public static ItemStack getRandomGlintBook(Random random) {
        List<String> keys = new ArrayList<>(GLINT_BOOKS.keySet());
        String color = keys.get(random.nextInt(keys.size()));

        ItemStack book = new ItemStack(GLINT_BOOKS.get(color));
        ColoredGlintHandler.applyStackColor(book, color);

        return book;
    }

    private void handleLootTables(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter) {
        if (id.equals(LootTables.STRONGHOLD_LIBRARY_CHEST)) {
            FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                .rolls(UniformLootTableRange.between(4.0F, 6.0F))
                .with(ItemEntry.builder(Items.BOOK)
                    .weight(1)
                    .apply(() -> new GlintBookLootFunction(new LootCondition[0])));

            supplier.pool(builder);
        }
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (!left.hasGlint() || !(right.getItem() instanceof GlintBookItem))
            return ActionResult.PASS;

        int cost = Math.max(0, xpCost);
        out = left.copy();

        String color = ColoredGlintHandler.getStackColor(right);
        ColoredGlintHandler.applyStackColor(out, color);

        apply.accept(out, cost, 1);
        return ActionResult.SUCCESS;
    }
}
