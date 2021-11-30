package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class GrindstoneEvents {
    private static final Map<UUID, GrindstoneMenuInstance> SERVER_INSTANCES = new WeakHashMap<>();
    private static final Map<UUID, GrindstoneMenuInstance> CLIENT_INSTANCES = new WeakHashMap<>();

    public static GrindstoneMenuInstance create(GrindstoneMenu menu, Player player, Inventory inventory, Container input, Container output, ContainerLevelAccess access) {
        GrindstoneMenuInstance instance = new GrindstoneMenuInstance(menu, player, inventory, input, output, access);
        putSidedInstance(player, instance);
        return instance;
    }

    public static void remove(Player player) {
        removeSidedInstance(player);
    }

    @Nullable
    public static GrindstoneMenuInstance instance(@Nullable Player player) {
        if (player == null) return null;
        return getSidedInstance(player);
    }

    public static Event<CalculateOutputInvoker> CALCULATE_OUTPUT = EventFactory.createArrayBacked(CalculateOutputInvoker.class, callbacks -> instance -> {
        for (CalculateOutputInvoker callback : callbacks) {
            boolean result = callback.invoke(instance);
            if (result) return true;
        }
        return false;
    });

    public static Event<CanPlaceInvoker> CAN_PLACE = EventFactory.createArrayBacked(CanPlaceInvoker.class, callbacks -> (container, stack) -> {
        for (CanPlaceInvoker callback : callbacks) {
            boolean result = callback.invoke(container, stack);
            if (result) return true;
        }
        return false;
    });

    public static Event<CanTakeInvoker> CAN_TAKE = EventFactory.createArrayBacked(CanTakeInvoker.class, callbacks -> (invoker, player) -> {
        for (CanTakeInvoker callback : callbacks) {
            InteractionResult result = callback.invoke(invoker, player);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    });

    public static Event<OnTakeInvoker> ON_TAKE = EventFactory.createArrayBacked(OnTakeInvoker.class, callbacks -> (invoker, player, stack) -> {
        for (OnTakeInvoker callback : callbacks) {
            boolean result = callback.invoke(invoker, player, stack);
            if (result) return true;
        }
        return false;
    });

    @Nullable
    private static GrindstoneMenuInstance getSidedInstance(Player player) {
        UUID uuid = player.getUUID();

        if (player.level.isClientSide && CLIENT_INSTANCES.containsKey(uuid)) {
            return CLIENT_INSTANCES.get(uuid);
        } else if (SERVER_INSTANCES.containsKey(uuid)) {
            return SERVER_INSTANCES.get(uuid);
        }

        return null;
    }

    private static void putSidedInstance(Player player, GrindstoneMenuInstance instance) {
        UUID uuid = player.getUUID();
        if (player.level.isClientSide) {
            CLIENT_INSTANCES.put(uuid, instance);
        } else {
            SERVER_INSTANCES.put(uuid, instance);
        }
    }

    private static void removeSidedInstance(Player player) {
        UUID uuid = player.getUUID();
        if (player.level.isClientSide) {
            CLIENT_INSTANCES.remove(uuid);
        } else {
            SERVER_INSTANCES.remove(uuid);
        }
    }

    @FunctionalInterface
    public interface CalculateOutputInvoker {
        boolean invoke(GrindstoneMenuInstance instance);
    }

    @FunctionalInterface
    public interface CanPlaceInvoker {
        boolean invoke(Container container, ItemStack stack);
    }

    @FunctionalInterface
    public interface CanTakeInvoker {
        InteractionResult invoke(GrindstoneMenuInstance instance, Player player);
    }

    @FunctionalInterface
    public interface OnTakeInvoker {
        boolean invoke(GrindstoneMenuInstance instance, Player player, ItemStack stack);
    }

    public static class GrindstoneMenuInstance {
        public GrindstoneMenu menu;
        public Player player;
        public Inventory inventory;
        public Container input;
        public Container output;
        public ContainerLevelAccess access;

        public GrindstoneMenuInstance(GrindstoneMenu menu, Player player, Inventory inventory, Container input, Container output, ContainerLevelAccess access) {
            this.menu = menu;
            this.player = player;
            this.inventory = inventory;
            this.input = input;
            this.output = output;
            this.access = access;
        }
    }
}
