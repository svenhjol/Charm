package svenhjol.charm.api.event;

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

@SuppressWarnings("unused")
public class GrindstoneEvents {
    private static final Map<UUID, GrindstoneMenuInstance> SERVER_INSTANCES = new WeakHashMap<>();
    private static final Map<UUID, GrindstoneMenuInstance> CLIENT_INSTANCES = new WeakHashMap<>();
    public static final CalculateOutputEvent CALCULATE_OUTPUT = new CalculateOutputEvent();
    public static final CanPlaceEvent CAN_PLACE = new CanPlaceEvent();
    public static final CanTakeEvent CAN_TAKE = new CanTakeEvent();
    public static final OnTakeEvent ON_TAKE = new OnTakeEvent();

    @Nullable
    public static GrindstoneMenuInstance instance(@Nullable Player player) {
        if (player == null) return null;
        return getSidedInstance(player);
    }

    public static GrindstoneMenuInstance create(GrindstoneMenu menu, Player player, Inventory inventory, Container input, Container output, ContainerLevelAccess access) {
        var instance = new GrindstoneMenuInstance(menu, player, inventory, input, output, access);
        putSidedInstance(player, instance);
        return instance;
    }

    public static void remove(Player player) {
        removeSidedInstance(player);
    }

    private static void putSidedInstance(Player player, GrindstoneMenuInstance instance) {
        UUID uuid = player.getUUID();
        if (player.level().isClientSide) {
            CLIENT_INSTANCES.put(uuid, instance);
        } else {
            SERVER_INSTANCES.put(uuid, instance);
        }
    }

    @Nullable
    private static GrindstoneMenuInstance getSidedInstance(Player player) {
        UUID uuid = player.getUUID();

        if (player.level().isClientSide && CLIENT_INSTANCES.containsKey(uuid)) {
            return CLIENT_INSTANCES.get(uuid);
        } else if (SERVER_INSTANCES.containsKey(uuid)) {
            return SERVER_INSTANCES.get(uuid);
        }

        return null;
    }

    private static void removeSidedInstance(Player player) {
        UUID uuid = player.getUUID();
        if (player.level().isClientSide) {
            CLIENT_INSTANCES.remove(uuid);
        } else {
            SERVER_INSTANCES.remove(uuid);
        }
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

    public static class CalculateOutputEvent extends CharmEvent<CalculateOutputEvent.Handler> {
        private CalculateOutputEvent() {}

        public boolean invoke(GrindstoneMenuInstance instance) {
            for (Handler handler : getHandlers()) {
                if (handler.run(instance)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(GrindstoneMenuInstance menu);
        }
    }

    public static class CanPlaceEvent extends CharmEvent<CanPlaceEvent.Handler> {
        private CanPlaceEvent() {}

        public boolean invoke(Container container, ItemStack itemStack) {
            for (Handler handler : getHandlers()) {
                if (handler.run(container, itemStack)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(Container container, ItemStack itemStack);
        }
    }

    public static class CanTakeEvent extends CharmEvent<CanTakeEvent.Handler> {
        private CanTakeEvent() {}

        public InteractionResult invoke(GrindstoneMenuInstance instance, Player player) {
            for (Handler handler : getHandlers()) {
                var result = handler.run(instance, player);
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }

            return InteractionResult.PASS;
        }

        @FunctionalInterface
        public interface Handler {
            InteractionResult run(GrindstoneMenuInstance instance, Player player);
        }
    }

    public static class OnTakeEvent extends CharmEvent<OnTakeEvent.Handler> {
        private OnTakeEvent() {}

        public boolean invoke(GrindstoneMenuInstance instance, Player player, ItemStack itemStack) {
            for (Handler handler : getHandlers()) {
                if (handler.run(instance, player, itemStack)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(GrindstoneMenuInstance instance, Player player, ItemStack itemStack);
        }
    }
}
