package svenhjol.charm.api.event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

@SuppressWarnings("unused")
public class BlockItemRenderEvent extends CharmEvent<BlockItemRenderEvent.Handler> {
    public static final BlockItemRenderEvent INSTANCE = new BlockItemRenderEvent();

    private BlockItemRenderEvent() {}

    public Optional<BlockEntity> invoke(ItemStack itemStack, Block block) {
        for (var handler : getHandlers()) {
            var result = handler.run(itemStack, block);

            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    @FunctionalInterface
    public interface Handler {
        Optional<BlockEntity> run(ItemStack itemStack, Block block);
    }
}
