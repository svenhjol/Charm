package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface RenderBlockItemCallback {
    Event<RenderBlockItemCallback> EVENT = EventFactory.createArrayBacked(RenderBlockItemCallback.class, listeners -> (stack, block) -> {
        for (RenderBlockItemCallback listener : listeners) {
            BlockEntity blockEntity = listener.interact(stack, block);
            if (blockEntity != null) return blockEntity;
        }
        return null;
    });

    @Nullable
    BlockEntity interact(ItemStack stack, Block block);
}
