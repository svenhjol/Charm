package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface BlockItemRenderCallback {
    Event<BlockItemRenderCallback> EVENT = EventFactory.createArrayBacked(BlockItemRenderCallback.class, (listeners) -> (block) -> {
        for (BlockItemRenderCallback listener : listeners) {
            BlockEntity blockEntity = listener.interact(block);
            if (blockEntity != null)
                return blockEntity;
        }

        return null;
    });

    @Nullable
    BlockEntity interact(Block block);
}
