package svenhjol.charm.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemUseEvent extends CharmEvent<ItemUseEvent.Handler> {
    public static final ItemUseEvent INSTANCE = new ItemUseEvent();

    private ItemUseEvent() {}

    public InteractionResultHolder<ItemStack> invoke(Player player, Level level, InteractionHand hand) {
        for (var handler : getHandlers()) {
            var holder = handler.run(player, level, hand);
            if (holder.getResult() != InteractionResult.PASS) {
                return holder;
            }
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResultHolder<ItemStack> run(Player player, Level level, InteractionHand hand);
    }
}
