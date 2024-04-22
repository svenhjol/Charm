package svenhjol.charm.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class AnvilRepairEvent extends CharmEvent<AnvilRepairEvent.Handler> {
    public static final AnvilRepairEvent INSTANCE = new AnvilRepairEvent();

    private AnvilRepairEvent() {}

    public boolean invoke(AnvilMenu menu, Player player, ItemStack leftStack, ItemStack rightStack) {
        for (var handler : getHandlers()) {
            var result = handler.run(menu, player, leftStack, rightStack);

            // Return true as soon as a handler satisfies the criteria.
            if (result) {
                return true;
            }
        }

        return false;
    }

    @FunctionalInterface
    public interface Handler {
        boolean run(AnvilMenu menu, Player player, ItemStack leftStack, ItemStack rightStack);
    }
}
