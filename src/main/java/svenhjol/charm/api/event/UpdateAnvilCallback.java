package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface UpdateAnvilCallback {
    Event<UpdateAnvilCallback> EVENT = EventFactory.createArrayBacked(UpdateAnvilCallback.class, (listeners) -> (
        menu, player, left, right, setOutput, setXpCost, setMaterialCost
    ) -> {
        for (UpdateAnvilCallback listener : listeners) {
            var result = listener.interact(
                menu, player, left, right, setOutput, setXpCost, setMaterialCost
            );
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(AnvilMenu menu, Player player, ItemStack left, ItemStack right,
                               Consumer<ItemStack> setOutput, Consumer<Integer> setXpCost, Consumer<Integer> setMaterialCost);
}
