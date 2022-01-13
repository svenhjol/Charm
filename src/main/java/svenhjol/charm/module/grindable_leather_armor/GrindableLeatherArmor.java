package svenhjol.charm.module.grindable_leather_armor;

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

import java.util.ArrayList;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Leather armor returns a single piece of leather when used on the grindstone.")
public class GrindableLeatherArmor extends CharmModule {
    public static final ResourceLocation TRIGGER_RECYCLED_LEATHER_ARMOR = new ResourceLocation(Charm.MOD_ID, "recycled_leather_armor");
    public static final List<Item> ARMOR_RECIPES = new ArrayList<>();

    @Override
    public void runWhenEnabled() {
        ARMOR_RECIPES.add(Items.LEATHER_HELMET);
        ARMOR_RECIPES.add(Items.LEATHER_CHESTPLATE);
        ARMOR_RECIPES.add(Items.LEATHER_LEGGINGS);
        ARMOR_RECIPES.add(Items.LEATHER_BOOTS);

        GrindstoneEvents.CAN_PLACE.register(this::handlePlaced);
        GrindstoneEvents.CALCULATE_OUTPUT.register(this::handleCalculatedOutput);
        GrindstoneEvents.ON_TAKE.register(this::handleOnTake);
    }

    private boolean handleCalculatedOutput(GrindstoneMenuInstance instance) {
        if (!Charm.LOADER.isEnabled(GrindableLeatherArmor.class)) return false;

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

        if (ARMOR_RECIPES.contains(slot0.getItem()) && slot1.isEmpty()) {
            instance.output.setItem(0, new ItemStack(Items.LEATHER));
        } else if (ARMOR_RECIPES.contains(slot1.getItem()) && slot0.isEmpty()) {
            instance.output.setItem(0, new ItemStack(Items.LEATHER));
        } else {
            return false;
        }

        instance.menu.broadcastChanges();
        return true;
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        if (player.level.isClientSide) return false;

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

        if (ARMOR_RECIPES.contains(slot0.getItem()) || ARMOR_RECIPES.contains(slot1.getItem())) {
            triggerRecycledLeatherArmor((ServerPlayer)player);
        }

        return false;
    }

    private boolean handlePlaced(Container container, ItemStack itemStack) {
        return Charm.LOADER.isEnabled(GrindableLeatherArmor.class) && ARMOR_RECIPES.contains(itemStack.getItem());
    }

    public static void triggerRecycledLeatherArmor(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_RECYCLED_LEATHER_ARMOR);
    }
}
