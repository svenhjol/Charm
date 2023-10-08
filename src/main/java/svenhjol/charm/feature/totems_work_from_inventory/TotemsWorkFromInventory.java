package svenhjol.charm.feature.totems_work_from_inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.ITotemInventoryCheckProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Feature(mod = Charm.MOD_ID, description = "A totem will work from anywhere in the player's inventory as well as held in the main or offhand.")
public class TotemsWorkFromInventory extends CharmonyFeature implements ITotemInventoryCheckProvider {
    static List<BiFunction<Player, ItemLike, ItemStack>> inventoryChecks = new ArrayList<>();

    @Override
    public void register() {
        ApiHelper.consume(ITotemInventoryCheckProvider.class,
            provider -> inventoryChecks.addAll(provider.getTotemInventoryChecks()));

        CharmonyApi.registerProvider(this);
    }

    public static ItemStack tryUsingTotemFromInventory(LivingEntity entity) {
        if (entity instanceof Player player) {
            for (var check : inventoryChecks) {
                var result = check.apply(player, Items.TOTEM_OF_UNDYING);
                if (!result.isEmpty()) {
                    triggerUsedTotemFromInventory(player, result);
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

    public static void triggerUsedTotemFromInventory(Player player, ItemStack stack) {
        // Trigger the advancement if a totem of undying and not in player's hand.
        if (stack.is(Items.TOTEM_OF_UNDYING)) {
            var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            var offHand = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!mainHand.is(Items.TOTEM_OF_UNDYING) && !offHand.is(Items.TOTEM_OF_UNDYING)) {
                Advancements.trigger(Charm.instance().makeId("used_totem_from_inventory"), player);
            }
        }
    }
}
