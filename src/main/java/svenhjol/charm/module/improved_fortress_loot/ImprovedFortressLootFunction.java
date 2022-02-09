package svenhjol.charm.module.improved_fortress_loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.module.improved_mansion_loot.ImprovedMansionLoot;

public class ImprovedFortressLootFunction extends LootItemConditionalFunction {
    protected ImprovedFortressLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(ImprovedMansionLoot.class)) return stack;

        var item = stack.getItem();
        var random = context.getRandom();
        var luck = context.getLuck();
        int level;
        ItemStack out;

        if (item == Items.BOOK) {
            level = 30;
            out = stack.copy();
        } else if (item == Items.GOLDEN_SWORD) {
            level = 20;
            item = ImprovedFortressLoot.TOOLS.get(random.nextInt(ImprovedFortressLoot.TOOLS.size()));
            out = new ItemStack(item);
        } else if (item == Items.GOLDEN_CHESTPLATE) {
            level = 20;
            item = ImprovedFortressLoot.ARMOR.get(random.nextInt(ImprovedFortressLoot.ARMOR.size()));
            out = new ItemStack(item);
        } else if (item == Items.DIAMOND || item == Items.BLAZE_POWDER) {
            return new ItemStack(item, random.nextInt(5) + 1);
        } else if (item == Items.WITHER_SKELETON_SKULL) {
            return random.nextFloat() < (0.025F + luck * 0.1F) ? stack : ItemStack.EMPTY;
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

    public static class Serializer extends LootItemConditionalFunction.Serializer<ImprovedFortressLootFunction> {
        @Override
        public ImprovedFortressLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new ImprovedFortressLootFunction(conditions);
        }
    }
}
