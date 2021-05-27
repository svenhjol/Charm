package svenhjol.charm.module.cooking_pots;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = CookingPotsClient.class, description = "Cooking pots let you combine up to 64 food items, keeping an average of all hunger and saturation. Use wooden bowls to extract stew from the pot.")
public class CookingPots extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "cooking_pot");
    public static final Identifier TRIGGER_LIT_FIRE = new Identifier(Charm.MOD_ID, "lit_fire_under_pot");
    public static final Identifier TRIGGER_FILLED_WATER = new Identifier(Charm.MOD_ID, "filled_pot_with_water");
    public static final Identifier TRIGGER_ADDED_ITEM = new Identifier(Charm.MOD_ID, "added_item_to_pot");
    public static final Identifier TRIGGER_TAKEN_FOOD = new Identifier(Charm.MOD_ID, "taken_food_from_pot");

    public static CookingPotBlock COOKING_POT;
    public static BlockEntityType<CookingPotBlockEntity> BLOCK_ENTITY;
    public static MixedStewItem MIXED_STEW;

    public static final Identifier MSG_CLIENT_ADDED_TO_POT = new Identifier(Charm.MOD_ID, "client_added_to_pot");

    @Override
    public void register() {
        COOKING_POT = new CookingPotBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, CookingPotBlockEntity::new, COOKING_POT);
        MIXED_STEW = new MixedStewItem(this);
    }

    public static List<Item> getResolvedItems(List<Identifier> ids) {
        List<Item> items = new ArrayList<>();

        for (Identifier id : ids) {
            Registry.ITEM.getOrEmpty(id).ifPresent(items::add);
        }

        return items;
    }

    public static void triggerLitFire(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_LIT_FIRE);
    }

    public static void triggerFilledWater(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FILLED_WATER);
    }

    public static void triggerAddedItem(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ADDED_ITEM);
    }

    public static void triggerTakenFood(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TAKEN_FOOD);
    }
}
