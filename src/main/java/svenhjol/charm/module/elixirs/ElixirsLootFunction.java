package svenhjol.charm.module.elixirs;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import svenhjol.charm.Charm;
import svenhjol.charm.module.elixirs.item.Elixir;

public class ElixirsLootFunction extends LootItemConditionalFunction {
    protected ElixirsLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!Charm.LOADER.isEnabled(Elixirs.class)) return stack;

        return tryCreate(stack, context);
    }

    private ItemStack tryCreate(ItemStack stack, LootContext context) {
        if (Elixirs.POTIONS.isEmpty()) return stack;

        var random = context.getRandom();

        // prefer the generic potions
        var genericPotion = Elixirs.POTIONS.stream().filter(p -> p.getClass() == Elixir.class).findFirst();
        if (genericPotion.isPresent() && random.nextFloat() < 0.75F) {
            return genericPotion.get().getPotionItem();
        }

        var potion = Elixirs.POTIONS.get(random.nextInt(Elixirs.POTIONS.size()));
        var out = potion.getPotionItem();
        out.getOrCreateTag().putBoolean(Elixirs.ELIXIR_TAG, true);

        return out;
    }

    @Override
    public LootItemFunctionType getType() {
        return Elixirs.LOOT_FUNCTION;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ElixirsLootFunction> {
        @Override
        public ElixirsLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new ElixirsLootFunction(conditions);
        }
    }
}
