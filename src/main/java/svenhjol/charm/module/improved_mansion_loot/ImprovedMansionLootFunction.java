package svenhjol.charm.module.improved_mansion_loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.helper.LogHelper;

public class ImprovedMansionLootFunction extends LootItemConditionalFunction {
    protected ImprovedMansionLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(ImprovedMansionLoot.class)) return stack;

        var item = stack.getItem();
        var random = context.getRandom();
        int level;
        ItemStack out;

        if (item == Items.BOOK) {
            var f = random.nextFloat();

            if (f < 0.5F) {
                var book = new ItemStack(Items.ENCHANTED_BOOK);
                var enchantment = f < 0.25F ? Enchantments.MENDING : Enchantments.INFINITY_ARROWS;
                EnchantmentsHelper.apply(book, enchantment, 1);
                return book;
            } else {
                level = 30;
                out = stack.copy();
            }
        } else if (item == Items.IRON_AXE) {
            level = 20;
            item = ImprovedMansionLoot.TOOLS.get(random.nextInt(ImprovedMansionLoot.TOOLS.size()));
            out = new ItemStack(item);
        } else if (item == Items.IRON_CHESTPLATE) {
            level = 20;
            item = ImprovedMansionLoot.ARMOR.get(random.nextInt(ImprovedMansionLoot.ARMOR.size()));
            out = new ItemStack(item);
        } else if (item == Items.EMERALD) {
            return new ItemStack(item, random.nextInt(5) + 1);
        } else {
            return stack;
        }

        out = EnchantmentHelper.enchantItem(random, out, level, true);

        if (EnchantmentHelper.getEnchantments(out).isEmpty()) {
            LogHelper.debug(getClass(), "Item did not get enchanted properly");
            return ItemStack.EMPTY;
        }

        return out;
    }

    @Override
    public LootItemFunctionType getType() {
        return ImprovedMansionLoot.LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ImprovedMansionLootFunction> {
        @Override
        public ImprovedMansionLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new ImprovedMansionLootFunction(conditions);
        }
    }
}
