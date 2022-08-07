package svenhjol.charm.module.totem_of_preserving;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;

public class TotemOfPreservingChestLootFunction extends LootItemConditionalFunction {
    protected TotemOfPreservingChestLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(TotemOfPreserving.class)) return stack;
        if (TotemOfPreserving.graveMode) return stack;
        return new ItemStack(TotemOfPreserving.ITEM);
    }

    @Override
    public LootItemFunctionType getType() {
        return TotemOfPreserving.CHEST_LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<TotemOfPreservingChestLootFunction> {
        @Override
        public TotemOfPreservingChestLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new TotemOfPreservingChestLootFunction(conditions);
        }
    }
}
