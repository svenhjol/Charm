package svenhjol.charm.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import svenhjol.charm.module.Tinted;

public class TintedEnchantmentLootFunction extends ConditionalLootFunction {

    public TintedEnchantmentLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(book, new EnchantmentLevelEntry(Tinted.TINTED, 1));
        return book;
    }

    @Override
    public LootFunctionType getType() {
        return Tinted.LOOT_FUNCTION;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<TintedEnchantmentLootFunction> {
        @Override
        public TintedEnchantmentLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new TintedEnchantmentLootFunction(conditions);
        }
    }
}
