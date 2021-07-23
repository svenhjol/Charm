package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

public interface ApplyBeaconEffectsEvent {
    Event<ApplyBeaconEffectsEvent> EVENT = EventFactory.createArrayBacked(ApplyBeaconEffectsEvent.class, (listeners) -> (world, level, pos, primary, secondary) -> {
        for (ApplyBeaconEffectsEvent listener : listeners) {
            listener.interact(world, level, pos, primary, secondary);
        }
    });

    void interact(Level world, BlockPos pos, int level, MobEffect primary, MobEffect secondary);
}
