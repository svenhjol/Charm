package svenhjol.charm.module;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.enchantment.TintedEnchantment;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.loot.TintedEnchantmentLootFunction;

@Module(mod = Charm.MOD_ID, description = "When applied, this enchantment lets you change the color of the enchanted glint using dye on an anvil. Requires Core 'Enchantment glint override' to be true.")
public class Tinted extends CharmModule {
    public static final Identifier LOOT_ID = new Identifier(Charm.MOD_ID, "tinted_book_loot");
    public static LootFunctionType LOOT_FUNCTION;
    public static TintedEnchantment TINTED;

    @Config(name = "XP cost", description = "Number of levels required to change a tinted item using dye on an anvil.")
    public static int xpCost = 0;

    @Override
    public void register() {
        TINTED = new TintedEnchantment(this);
        LOOT_FUNCTION = RegistryHandler.lootFunctionType(LOOT_ID, new LootFunctionType(new TintedEnchantmentLootFunction.Serializer()));
    }

    @Override
    public boolean depends() {
        return Core.overrideGlint;
    }

    @Override
    public void init() {
        if (!ModuleHandler.enabled("charm:anvil_improvements") && xpCost < 1)
            xpCost = 1;

        // listen for anvil behavior
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);

        // listen for loot table generation
        LootTableLoadingCallback.EVENT.register(this::handleLootTables);
    }

    /**
     * Adds the enchantment and color directly to the input stack with no sanity checking.
     */
    public static void applyTint(ItemStack stack, String color) {
        EnchantmentsHelper.apply(stack, TINTED, 1);
        stack.getOrCreateTag().putString(ColoredGlintHandler.GLINT_TAG, color);
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (!EnchantmentsHelper.has(left, TINTED) || !(right.getItem() instanceof DyeItem))
            return ActionResult.PASS;

        int cost = Math.max(0, xpCost);
        out = left.copy();
        DyeItem dye = (DyeItem)right.getItem();
        String color = dye.getColor().asString();

        applyTint(out, color);
        apply.accept(out, cost, 1);

        return ActionResult.SUCCESS;
    }

    private void handleLootTables(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter) {
        if (id.equals(LootTables.STRONGHOLD_LIBRARY_CHEST)) {
            FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootTableRange.create(1))
                .with(ItemEntry.builder(Items.BOOK)
                    .weight(1)
                    .apply(() -> new TintedEnchantmentLootFunction(new LootCondition[0])));

            supplier.pool(builder);
        }
    }
}
