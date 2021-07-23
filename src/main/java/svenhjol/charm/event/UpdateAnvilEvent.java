package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

public interface UpdateAnvilEvent {
    Event<UpdateAnvilEvent> EVENT = EventFactory.createArrayBacked(UpdateAnvilEvent.class, (listeners) -> (handler, player, left, right, output, name, baseCost, apply) -> {
        for (UpdateAnvilEvent listener : listeners) {
            InteractionResult result = listener.interact(handler, player, left, right, output, name, baseCost, apply);
            if (result == InteractionResult.SUCCESS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(AnvilMenu handler, Player player, ItemStack left, ItemStack right, Container output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply);
}
