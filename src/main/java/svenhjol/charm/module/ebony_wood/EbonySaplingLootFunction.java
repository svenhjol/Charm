package svenhjol.charm.module.ebony_wood;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EbonySaplingLootFunction extends LootItemConditionalFunction {
    protected EbonySaplingLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        return new ItemStack(EbonyWood.SAPLING);
    }

    @Override
    public LootItemFunctionType getType() {
        return EbonyWood.LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EbonySaplingLootFunction> {
        @Override
        public EbonySaplingLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new EbonySaplingLootFunction(conditions);
        }
    }
}
