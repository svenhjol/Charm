package svenhjol.charm.api.event;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class SmithingTableEvents {
    static final Map<UUID, SmithingTableInstance> SERVER_INSTANCES = new WeakHashMap<>();
    static final Map<UUID, SmithingTableInstance> CLIENT_INSTANCES = new WeakHashMap<>();
    public static final ValidateTemplateEvent VALIDATE_TEMPLATE = new ValidateTemplateEvent();
    public static final CanPlaceEvent CAN_PLACE = new CanPlaceEvent();
    public static final CalculateOutputEvent CALCULATE_OUTPUT = new CalculateOutputEvent();
    public static final CanTakeEvent CAN_TAKE = new CanTakeEvent();
    public static final OnTakeEvent ON_TAKE = new OnTakeEvent();

    public static class SmithingTableInstance {
        public SmithingMenu menu;
        public Player player;
        public Container input;
        public ResultContainer output;
        public Inventory inventory;
        public ContainerLevelAccess access;

        public SmithingTableInstance(SmithingMenu menu, Player player, Inventory inventory, Container input,
                                     ResultContainer output, ContainerLevelAccess access) {
            this.menu = menu;
            this.player = player;
            this.input = input;
            this.output = output;
            this.inventory = inventory;
            this.access = access;
        }
    }

    @Nullable
    public static SmithingTableInstance instance(@Nullable Player player) {
        if (player == null) return null;
        return getSidedInstance(player);
    }

    public static SmithingTableInstance create(SmithingMenu menu, Player player, Inventory inventory, Container input,
                                               ResultContainer output, ContainerLevelAccess access) {
        var instance = new SmithingTableInstance(menu, player, inventory, input, output, access);
        putSidedInstance(player, instance);
        return instance;
    }

    public static void remove(Player player) {
        removeSidedInstance(player);
    }

    static void putSidedInstance(Player player, SmithingTableInstance instance) {
        var uuid = player.getUUID();
        if (player.level().isClientSide) {
            CLIENT_INSTANCES.put(uuid, instance);
        } else {
            SERVER_INSTANCES.put(uuid, instance);
        }
    }

    @Nullable
    static SmithingTableInstance getSidedInstance(Player player) {
        var uuid = player.getUUID();

        if (player.level().isClientSide && CLIENT_INSTANCES.containsKey(uuid)) {
            return CLIENT_INSTANCES.get(uuid);
        } else if (SERVER_INSTANCES.containsKey(uuid)) {
            return SERVER_INSTANCES.get(uuid);
        }

        return null;
    }

    static void removeSidedInstance(Player player) {
        var uuid = player.getUUID();
        if (player.level().isClientSide) {
            CLIENT_INSTANCES.remove(uuid);
        } else {
            SERVER_INSTANCES.remove(uuid);
        }
    }

    public static class CanPlaceEvent extends svenhjol.charm.api.event.CharmEvent<CanPlaceEvent.Handler> {
        private CanPlaceEvent() {}

        public boolean invoke(ItemStack template, Integer slot, ItemStack stack) {
            for (Handler handler : getHandlers()) {
                if (handler.run(template, slot, stack)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(ItemStack template, Integer slot, ItemStack stack);
        }
    }

    public static class ValidateTemplateEvent extends svenhjol.charm.api.event.CharmEvent<ValidateTemplateEvent.Handler> {
        private ValidateTemplateEvent() {}

        public boolean invoke(ItemStack stack) {
            for (Handler handler : getHandlers()) {
                if (handler.run(stack)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(ItemStack stack);
        }
    }

    public static class CalculateOutputEvent extends svenhjol.charm.api.event.CharmEvent<CalculateOutputEvent.Handler> {
        private CalculateOutputEvent() {}

        public boolean invoke(SmithingTableInstance instance) {
            for (Handler handler : getHandlers()) {
                if (handler.run(instance)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(SmithingTableInstance menu);
        }
    }

    public static class CanTakeEvent extends svenhjol.charm.api.event.CharmEvent<CanTakeEvent.Handler> {
        private CanTakeEvent() {}

        public InteractionResult invoke(SmithingTableInstance instance, Player player) {
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
            InteractionResult run(SmithingTableInstance instance, Player player);
        }
    }

    public static class OnTakeEvent extends CharmEvent<OnTakeEvent.Handler> {
        private OnTakeEvent() {}

        public boolean invoke(SmithingTableInstance instance, Player player, ItemStack stack) {
            for (Handler handler : getHandlers()) {
                if (handler.run(instance, player, stack)) {
                    return true;
                }
            }

            return false;
        }

        @FunctionalInterface
        public interface Handler {
            boolean run(SmithingTableInstance instance, Player player, ItemStack itemStack);
        }
    }
}