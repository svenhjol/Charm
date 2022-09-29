package svenhjol.charm.module.grindable_armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.api.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Armor returns a single ingot, leather or diamond when used on the grindstone.")
public class GrindableArmor extends CharmModule {
    public static final ResourceLocation TRIGGER_RECYCLED_ARMOR = new ResourceLocation(Charm.MOD_ID, "recycled_armor");
    public static final Map<Item, Item> ARMOR_RECIPES = new HashMap<>();

    @Override
    public void runWhenEnabled() {
        ARMOR_RECIPES.put(Items.SADDLE, Items.LEATHER);
        ARMOR_RECIPES.put(Items.LEATHER_HORSE_ARMOR, Items.LEATHER);
        ARMOR_RECIPES.put(Items.LEATHER_HELMET, Items.LEATHER);
        ARMOR_RECIPES.put(Items.LEATHER_CHESTPLATE, Items.LEATHER);
        ARMOR_RECIPES.put(Items.LEATHER_LEGGINGS, Items.LEATHER);
        ARMOR_RECIPES.put(Items.LEATHER_BOOTS, Items.LEATHER);
        ARMOR_RECIPES.put(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.IRON_HELMET, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.IRON_CHESTPLATE, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.IRON_LEGGINGS, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.IRON_BOOTS, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.CHAINMAIL_HELMET, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.CHAINMAIL_CHESTPLATE, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.CHAINMAIL_LEGGINGS, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.CHAINMAIL_BOOTS, Items.IRON_INGOT);
        ARMOR_RECIPES.put(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);
        ARMOR_RECIPES.put(Items.GOLDEN_HELMET, Items.GOLD_INGOT);
        ARMOR_RECIPES.put(Items.GOLDEN_CHESTPLATE, Items.GOLD_INGOT);
        ARMOR_RECIPES.put(Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT);
        ARMOR_RECIPES.put(Items.GOLDEN_BOOTS, Items.GOLD_INGOT);
        ARMOR_RECIPES.put(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);
        ARMOR_RECIPES.put(Items.DIAMOND_HELMET, Items.DIAMOND);
        ARMOR_RECIPES.put(Items.DIAMOND_CHESTPLATE, Items.DIAMOND);
        ARMOR_RECIPES.put(Items.DIAMOND_LEGGINGS, Items.DIAMOND);
        ARMOR_RECIPES.put(Items.DIAMOND_BOOTS, Items.DIAMOND);
        ARMOR_RECIPES.put(Items.NETHERITE_HELMET, Items.NETHERITE_SCRAP);
        ARMOR_RECIPES.put(Items.NETHERITE_CHESTPLATE, Items.NETHERITE_SCRAP);
        ARMOR_RECIPES.put(Items.NETHERITE_LEGGINGS, Items.NETHERITE_SCRAP);
        ARMOR_RECIPES.put(Items.NETHERITE_BOOTS, Items.NETHERITE_SCRAP);

        GrindstoneEvents.CAN_PLACE.register(this::handlePlaced);
        GrindstoneEvents.CALCULATE_OUTPUT.register(this::handleCalculatedOutput);
        GrindstoneEvents.ON_TAKE.register(this::handleOnTake);
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        if (player.level.isClientSide) return false;

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

        if (ARMOR_RECIPES.containsKey(slot0.getItem()) || ARMOR_RECIPES.containsKey(slot1.getItem())) {
            triggerRecycledArmor((ServerPlayer)player);
        }

        return false;
    }

    private boolean handleCalculatedOutput(GrindstoneMenuInstance instance) {
        if (!Charm.LOADER.isEnabled(GrindableArmor.class)) return false;

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

        if (slot0.isEnchanted() || slot1.isEnchanted()) {
            return false;
        }

        if (ARMOR_RECIPES.containsKey(slot0.getItem()) && slot0.getDamageValue() == 0 && slot1.isEmpty()) {
            instance.output.setItem(0, new ItemStack(ARMOR_RECIPES.get(slot0.getItem())));
        } else if (ARMOR_RECIPES.containsKey(slot1.getItem()) && slot1.getDamageValue() == 0 && slot0.isEmpty()) {
            instance.output.setItem(0, new ItemStack(ARMOR_RECIPES.get(slot1.getItem())));
        } else {
            return false;
        }

        instance.menu.broadcastChanges();
        return true;
    }

    private boolean handlePlaced(Container container, ItemStack stack) {
        return Charm.LOADER.isEnabled(GrindableArmor.class) && ARMOR_RECIPES.containsKey(stack.getItem());
    }

    public static void triggerRecycledArmor(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_RECYCLED_ARMOR);
    }
}
