package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;

public interface UpdateAnvilCallback {
    Event<UpdateAnvilCallback> EVENT = EventFactory.createArrayBacked(UpdateAnvilCallback.class, (listeners) -> (handler, player, left, right, output, name, baseCost, apply) -> {
        for (UpdateAnvilCallback listener : listeners) {
            ActionResult result = listener.interact(handler, player, left, right, output, name, baseCost, apply);
            if (result == ActionResult.SUCCESS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(AnvilScreenHandler handler, PlayerEntity player, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply);
}
