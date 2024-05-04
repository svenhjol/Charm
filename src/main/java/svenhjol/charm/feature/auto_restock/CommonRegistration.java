package svenhjol.charm.feature.auto_restock;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.helper.EnchantmentHelper;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class CommonRegistration extends Registration<AutoRestock> {
    // Remember which items were in our hands and how often they were used.
    private final Map<Player, EnumMap<InteractionHand, StackData>> handCache = new WeakHashMap<>();

    public CommonRegistration(AutoRestock feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (player.level().isClientSide()) return;

        var serverPlayer = (ServerPlayer)player;
        var cached = handCache.computeIfAbsent(serverPlayer, it -> new EnumMap<>(InteractionHand.class));

        for (var hand : InteractionHand.values()) {
            var stackData = cached.get(hand);
            if (stackData != null
                && serverPlayer.getItemInHand(hand).isEmpty()
                && getItemUsed(serverPlayer, stackData.item) > stackData.used) {
                findReplacement(serverPlayer, hand, stackData);
            }
            updateCache(serverPlayer, hand, cached);
        }
    }

    private int getItemUsed(ServerPlayer player, Item item) {
        return player.getStats().getValue(Stats.ITEM_USED.get(item));
    }

    private void findReplacement(ServerPlayer player, InteractionHand hand, StackData stackData) {
        var inventory = player.getInventory();

        // first 9 slots are the hotbar, anything from 36 is not inventory
        for (int i = 9; i < Math.min(36, inventory.getContainerSize()); i++) {
            var possibleReplacement = inventory.getItem(i);
            if (stackData.item == possibleReplacement.getItem()
                && EnchantmentHelper.containsSameEnchantments(possibleReplacement, stackData.enchantments)) {
                player.setItemInHand(hand, possibleReplacement.copy());
                inventory.removeItem(i, inventory.getMaxStackSize());
                AutoRestock.triggerRestockedCurrentItem(player);
                break;
            }
        }
    }

    private void updateCache(ServerPlayer player, InteractionHand hand, EnumMap<InteractionHand, StackData> cached) {
        var stack = player.getItemInHand(hand);

        if (stack.isEmpty()) {
            cached.put(hand, null);
        } else {
            var item = stack.getItem();
            var used = getItemUsed(player, item);
            var enchantments = stack.get(DataComponents.ENCHANTMENTS);
            var stackData = cached.computeIfAbsent(hand, k -> new StackData());

            stackData.item = item;
            stackData.enchantments = enchantments;
            stackData.used = used;
        }
    }
}
