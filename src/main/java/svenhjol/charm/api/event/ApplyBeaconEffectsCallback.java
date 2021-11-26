package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

/**
 * @version 4.0.0
 */
public interface ApplyBeaconEffectsCallback {
    Event<ApplyBeaconEffectsCallback> EVENT = EventFactory.createArrayBacked(ApplyBeaconEffectsCallback.class, (listeners) -> (level, pos, beaconLevel, primary, secondary) -> {
        for (ApplyBeaconEffectsCallback listener : listeners) {
            listener.interact(level, pos, beaconLevel, primary, secondary);
        }
    });

    void interact(Level level, BlockPos pos, int beaconLevel, MobEffect primary, MobEffect secondary);
}
