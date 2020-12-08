package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.PlayerTickCallback;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

@Module(mod = Charm.MOD_ID, description = "Refills your hotbar from your inventory.")
public class AutoRestock extends CharmModule {
    //remember which items were in our hands and how often they were used
    private final Map<PlayerEntity, EnumMap<Hand, StackData>> handCache = new WeakHashMap<>();

    @Override
    public void init() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    public static void addItemUsedStat(PlayerEntity player, ItemStack stack) {
        if (ModuleHandler.enabled(AutoRestock.class))
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
    }

    private void handlePlayerTick(PlayerEntity playerIn) {
        if (!playerIn.world.isClient) {
            ServerPlayerEntity player = (ServerPlayerEntity)playerIn;
            EnumMap<Hand, StackData> cached = handCache.computeIfAbsent(player, it -> new EnumMap<>(Hand.class));
            for (Hand hand : Hand.values()) {
                StackData stackData = cached.get(hand);
                if (stackData != null && player.getStackInHand(hand).isEmpty() && getItemUsed(player, stackData.item) > stackData.used) {
                    findReplacement(player, hand, stackData);
                }
                updateCache(player, hand, cached);
            }
        }
    }

    private void updateCache(ServerPlayerEntity player, Hand hand, EnumMap<Hand, StackData> cached) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            cached.put(hand, null);
        } else {
            Item item = stack.getItem();
            int used = getItemUsed(player, item);
            ListTag enchantments = stack.getEnchantments();
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

    private int getItemUsed(ServerPlayerEntity player, Item item) {
        return player.getStatHandler().getStat(Stats.USED.getOrCreateStat(item));
    }

    private void findReplacement(ServerPlayerEntity player, Hand hand, StackData stackData) {
        PlayerInventory inventory = player.inventory;

        //first 9 slots are the hotbar
        if (inventory != null) {
            for (int i = 9; i < inventory.size(); i++) {
                ItemStack possibleReplacement = inventory.getStack(i);
                if (stackData.item == possibleReplacement.getItem() && Objects.equals(stackData.enchantments, possibleReplacement.getEnchantments())) {
                    player.setStackInHand(hand, possibleReplacement.copy());
                    inventory.removeStack(i, inventory.getMaxCountPerStack());
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
