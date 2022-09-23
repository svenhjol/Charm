package svenhjol.charm.module.totem_works_from_inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@CommonModule(mod = Charm.MOD_ID, description = "A totem will work from anywhere in the player's inventory as well as held in the main or offhand.")
public class TotemWorksFromInventory extends CharmModule {
    private static final List<Function<Player, ItemStack>> INVENTORY_CHECKS = new ArrayList<>();
    private static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);

    @Override
    public void runWhenEnabled() {
        // Main hand check.
        registerInventoryCheck(player -> {
            var stack = player.getMainHandItem();
            return stack.getItem() == Items.TOTEM_OF_UNDYING ? stack : ItemStack.EMPTY;
        });

        // Off-hand check.
        registerInventoryCheck(player -> {
            var stack = player.getOffhandItem();
            return stack.getItem() == Items.TOTEM_OF_UNDYING ? stack : ItemStack.EMPTY;
        });

        // Main inventory check.
        registerInventoryCheck(player -> {
            var inventory = player.getInventory();
            var slotMatchingItem = inventory.findSlotMatchingItem(TOTEM);

            if (slotMatchingItem > 0) {
                return inventory.getItem(slotMatchingItem);
            }

            return ItemStack.EMPTY;
        });
    }

    /**
     * Use this method to register a callback to check a
     * custom inventory for a totem of undying.
     */
    public static void registerInventoryCheck(Function<Player, ItemStack> func) {
        INVENTORY_CHECKS.add(func);
    }

    public static ItemStack tryUsingTotemFromInventory(LivingEntity entity, InteractionHand hand) {
        if (Charm.LOADER.isEnabled(TotemWorksFromInventory.class) && entity instanceof Player player) {
            for (Function<Player, ItemStack> check : INVENTORY_CHECKS) {
                var result = check.apply(player);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }
}
