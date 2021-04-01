package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ApplyBeaconEffectsCallback {
    Event<ApplyBeaconEffectsCallback> EVENT = EventFactory.createArrayBacked(ApplyBeaconEffectsCallback.class, (listeners) -> (world, level, pos, primary, secondary) -> {
        for (ApplyBeaconEffectsCallback listener : listeners) {
            listener.interact(world, level, pos, primary, secondary);
        }
    });

    void interact(World world, BlockPos pos, int level, StatusEffect primary, StatusEffect secondary);
}
