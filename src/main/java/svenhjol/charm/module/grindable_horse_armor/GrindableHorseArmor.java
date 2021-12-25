package svenhjol.charm.module.grindable_horse_armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Horse armor returns a single ingot or gem when used on the grindstone.")
public class GrindableHorseArmor extends CharmModule {
    public static final ResourceLocation TRIGGER_RECYCLED_HORSE_ARMOR = new ResourceLocation(Charm.MOD_ID, "recycled_horse_armor");
    public static final Map<Item, Item> HORSE_ARMOR_RECIPES = new HashMap<>();

    @Override
    public void runWhenEnabled() {
        HORSE_ARMOR_RECIPES.put(Items.LEATHER_HORSE_ARMOR, Items.LEATHER);
        HORSE_ARMOR_RECIPES.put(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);
        HORSE_ARMOR_RECIPES.put(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);
        HORSE_ARMOR_RECIPES.put(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);
        HORSE_ARMOR_RECIPES.put(Items.SADDLE, Items.LEATHER);

        GrindstoneEvents.CAN_PLACE.register(this::handlePlacedInGrindstone);
        GrindstoneEvents.CALCULATE_OUTPUT.register(this::handleCalculateGrindstoneOutput);
    }

    private boolean handleCalculateGrindstoneOutput(GrindstoneEvents.GrindstoneMenuInstance instance) {
        if (!Charm.LOADER.isEnabled(GrindableHorseArmor.class)) return false;

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

        if (HORSE_ARMOR_RECIPES.containsKey(slot0.getItem()) && slot1.isEmpty()) {
            instance.output.setItem(0, new ItemStack(HORSE_ARMOR_RECIPES.get(slot0.getItem())));
        } else if (HORSE_ARMOR_RECIPES.containsKey(slot1.getItem()) && slot0.isEmpty()) {
            instance.output.setItem(0, new ItemStack(HORSE_ARMOR_RECIPES.get(slot1.getItem())));
        } else {
            return false;
        }

        instance.menu.broadcastChanges();
        return true;
    }

    private boolean handlePlacedInGrindstone(Container container, ItemStack stack) {
        return Charm.LOADER.isEnabled(GrindableHorseArmor.class) && GrindableHorseArmor.HORSE_ARMOR_RECIPES.containsKey(stack.getItem());
    }

    public static void triggerRecycledHorseArmor(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_RECYCLED_HORSE_ARMOR);
    }
}
