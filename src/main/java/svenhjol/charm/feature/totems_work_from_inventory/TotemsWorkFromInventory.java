package svenhjol.charm.feature.totems_work_from_inventory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmapi.Charmapi;
import svenhjol.charmapi.iface.ITotemInventoryCheckProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Feature(mod = Charm.MOD_ID, description = "A totem will work from anywhere in the player's inventory as well as held in the main or offhand.")
public class TotemsWorkFromInventory extends CharmFeature implements ITotemInventoryCheckProvider {
    static List<BiFunction<Player, ItemLike, ItemStack>> inventoryChecks = new ArrayList<>();

    @Override
    public void register() {
        ApiHelper.consume(ITotemInventoryCheckProvider.class,
            provider -> inventoryChecks.addAll(provider.getTotemInventoryChecks()));

        Charmapi.registerProvider(this);
    }

    public static ItemStack tryUsingTotemFromInventory(LivingEntity entity) {
        if (entity instanceof Player player) {
            for (var check : inventoryChecks) {
                var result = check.apply(player, Items.TOTEM_OF_UNDYING);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public List<BiFunction<Player, ItemLike, ItemStack>> getTotemInventoryChecks() {
        return List.of(
            // Main hand check.
            (player, item) -> {
                var stack = player.getMainHandItem();
                return stack.is((Item) item) ? stack : ItemStack.EMPTY;
            },

            // Off-hand check.
            (player, item) -> {
                var stack = player.getOffhandItem();
                return stack.is((Item) item) ? stack : ItemStack.EMPTY;
            },

            // Main inventory check.
            (player, item) -> {
                var inventory = player.getInventory();
                var slotMatchingItem = inventory.findSlotMatchingItem(new ItemStack(item));

                if (slotMatchingItem > 0) {
                    return inventory.getItem(slotMatchingItem);
                }

                return ItemStack.EMPTY;
            }
        );
    }
}
