package svenhjol.charm.module.cooking_pots;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.CharmModule;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = CookingPotsClient.class, description = "Cooking pots let you combine up to 64 food items, keeping an average of all hunger and saturation. Use wooden bowls to extract stew from the pot.")
public class CookingPots extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "cooking_pot");
    public static final ResourceLocation TRIGGER_LIT_FIRE = new ResourceLocation(Charm.MOD_ID, "lit_fire_under_pot");
    public static final ResourceLocation TRIGGER_FILLED_WATER = new ResourceLocation(Charm.MOD_ID, "filled_pot_with_water");
    public static final ResourceLocation TRIGGER_ADDED_ITEM = new ResourceLocation(Charm.MOD_ID, "added_item_to_pot");
    public static final ResourceLocation TRIGGER_TAKEN_FOOD = new ResourceLocation(Charm.MOD_ID, "taken_food_from_pot");

    public static CookingPotBlock COOKING_POT;
    public static BlockEntityType<CookingPotBlockEntity> BLOCK_ENTITY;
    public static MixedStewItem MIXED_STEW;

    @Config(name = "Maximum portions", description = "Maximum number of portions a cooking pot can hold.")
    public static int maxPortions = 64;

    public static final ResourceLocation MSG_CLIENT_ADDED_TO_POT = new ResourceLocation(Charm.MOD_ID, "client_added_to_pot");

    @Config(name = "Show label", description = "If true, cooking pots show their custom name and capacity as a hovering label. Requires the 'Storage Labels' feature to be enabled.")
    public static boolean showLabel = true;

    @Override
    public void register() {
        COOKING_POT = new CookingPotBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, CookingPotBlockEntity::new, COOKING_POT);
        MIXED_STEW = new MixedStewItem(this);
    }

    public static List<Item> getResolvedItems(List<ResourceLocation> ids) {
        List<Item> items = new ArrayList<>();

        for (ResourceLocation id : ids) {
            Registry.ITEM.getOptional(id).ifPresent(items::add);
        }

        return items;
    }

    public static void triggerLitFire(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_LIT_FIRE);
    }

    public static void triggerFilledWater(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FILLED_WATER);
    }

    public static void triggerAddedItem(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ADDED_ITEM);
    }

    public static void triggerTakenFood(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TAKEN_FOOD);
    }
}
