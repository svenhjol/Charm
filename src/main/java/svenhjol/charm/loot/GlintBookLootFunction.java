package svenhjol.charm.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import svenhjol.charm.module.GlintBooks;

public class GlintBookLootFunction extends ConditionalLootFunction {

    public GlintBookLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        return GlintBooks.getRandomGlintBook(context.getRandom());
    }

    @Override
    public LootFunctionType getType() {
        return GlintBooks.LOOT_FUNCTION;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<GlintBookLootFunction> {
        @Override
        public GlintBookLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new GlintBookLootFunction(conditions);
        }
    }
}
