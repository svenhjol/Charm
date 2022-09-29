package svenhjol.charm.module.rare_enchantments;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;
import svenhjol.charm.module.colored_glints.ColoredGlints;

import java.util.List;
import java.util.Map;

public class RareEnchantmentLootFunction extends LootItemConditionalFunction {
    protected RareEnchantmentLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(RareEnchantments.class)) return stack;
        return tryCreate(stack, context);
    }

    private ItemStack tryCreate(ItemStack stack, LootContext context) {
        var enchantments = RareEnchantments.ENCHANTMENTS;
        if (enchantments.isEmpty()) return stack;

        var random = context.getRandom();
        var enchantment = enchantments.get(random.nextInt(enchantments.size()));
        var maxLevel = enchantment.getMaxLevel();
        var book = new ItemStack(Items.ENCHANTED_BOOK);
        var map = Map.of(enchantment, maxLevel + 1);

        EnchantmentHelper.setEnchantments(map, book);
        RareEnchantments.applyTag(book);

        if (Charm.LOADER.isEnabled(ColoredGlints.class)) {
            var colors = List.of(DyeColor.values());
            var color = colors.get(random.nextInt(colors.size()));
            ColoredGlints.applyColoredGlint(book, color);
        }

        return book;
    }

    @Override
    public LootItemFunctionType getType() {
        return RareEnchantments.LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RareEnchantmentLootFunction> {
        @Override
        public RareEnchantmentLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new RareEnchantmentLootFunction(conditions);
        }
    }
}
