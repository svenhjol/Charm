package svenhjol.charm.module.auto_restock;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.event.PlayerTickCallback;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Module(mod = Charm.MOD_ID, description = "Refills your hotbar from your inventory.",
    requiresMixins = {"PlayerTickCallback"})
public class AutoRestock extends CharmModule {
    //remember which items were in our hands and how often they were used
    private final Map<Player, EnumMap<InteractionHand, StackData>> handCache = new WeakHashMap<>();

    @Override
    public void init() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    public static void addItemUsedStat(Player player, ItemStack stack) {
        if (ModuleHandler.enabled(AutoRestock.class))
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }

    private void handlePlayerTick(Player playerIn) {
        if (!playerIn.level.isClientSide) {
            ServerPlayer player = (ServerPlayer)playerIn;
            EnumMap<InteractionHand, StackData> cached = handCache.computeIfAbsent(player, it -> new EnumMap<>(InteractionHand.class));
            for (InteractionHand hand : InteractionHand.values()) {
                StackData stackData = cached.get(hand);
                if (stackData != null && player.getItemInHand(hand).isEmpty() && getItemUsed(player, stackData.item) > stackData.used) {
                    findReplacement(player, hand, stackData);
                }
                updateCache(player, hand, cached);
            }
        }
    }

    private void updateCache(ServerPlayer player, InteractionHand hand, EnumMap<InteractionHand, StackData> cached) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            cached.put(hand, null);
        } else {
            Item item = stack.getItem();
            int used = getItemUsed(player, item);
            ListTag enchantments = stack.getEnchantmentTags();
            StackData stackData = cached.get(hand);
            if (stackData == null) {
                stackData = new StackData();
                cached.put(hand, stackData);
            }
            stackData.item = item;
            stackData.enchantments = enchantments;
            stackData.used = used;
        }
    }

    private int getItemUsed(ServerPlayer player, Item item) {
        return player.getStats().getValue(Stats.ITEM_USED.get(item));
    }

    private void findReplacement(ServerPlayer player, InteractionHand hand, StackData stackData) {
        Inventory inventory = PlayerHelper.getInventory(player);

        //first 9 slots are the hotbar
        if (inventory != null) {
            for (int i = 9; i < inventory.getContainerSize(); i++) {
                ItemStack possibleReplacement = inventory.getItem(i);
                if (stackData.item == possibleReplacement.getItem() && Objects.equals(stackData.enchantments, possibleReplacement.getEnchantmentTags())) {
                    player.setItemInHand(hand, possibleReplacement.copy());
                    inventory.removeItem(i, inventory.getMaxStackSize());
                    break;
                }
            }
        }
    }

    private static class StackData {
        private Item item;
        private ListTag enchantments;
        private int used;
    }

}
