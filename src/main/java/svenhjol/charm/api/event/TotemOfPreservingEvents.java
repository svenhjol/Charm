package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

/**
 * @version 4.0.0
 */
public class TotemOfPreservingEvents {
    public static Event<BeforeAddStackInvoker> BEFORE_ADD_STACK = EventFactory.createArrayBacked(BeforeAddStackInvoker.class, (listeners) -> (player, stack) -> {
        for (BeforeAddStackInvoker listener : listeners) {
            InteractionResult result = listener.invoke(player, stack);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    });

    public static Event<BeforeCreateInvoker> BEFORE_CREATE = EventFactory.createArrayBacked(BeforeCreateInvoker.class, (listeners) -> (player, pos, totem) -> {
        for (BeforeCreateInvoker listener : listeners) {
            InteractionResult result = listener.invoke(player, pos, totem);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    });

    public static Event<AfterCreateInvoker> AFTER_CREATE = EventFactory.createArrayBacked(AfterCreateInvoker.class, (listeners) -> (player, pos, totem) -> {
        for (AfterCreateInvoker listener : listeners) {
            listener.invoke(player, pos, totem);
        }
    });

    @FunctionalInterface
    public interface BeforeAddStackInvoker {
        InteractionResult invoke(ServerPlayer player, ItemStack stack);
    }

    @FunctionalInterface
    public interface BeforeCreateInvoker {
        InteractionResult invoke(ServerPlayer player, BlockPos pos, ItemStack totem);
    }

    @FunctionalInterface
    public interface AfterCreateInvoker {
        void invoke(ServerPlayer player, BlockPos pos, ItemStack totem);
    }
}
