package svenhjol.charm.module.totem_of_preserving;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;

import java.util.Random;

public class TotemOfPreservingLootFunction extends LootItemConditionalFunction {
    protected TotemOfPreservingLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(TotemOfPreserving.class)) return stack;
        if (TotemOfPreserving.isGraveMode(context.getLevel().getDifficulty())) return stack;

        return new ItemStack(TotemOfPreserving.TOTEM_OF_PRESERVING);
    }

    @Override
    public LootItemFunctionType getType() {
        return TotemOfPreserving.LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<TotemOfPreservingLootFunction> {
        @Override
        public TotemOfPreservingLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new TotemOfPreservingLootFunction(conditions);
        }
    }
}
