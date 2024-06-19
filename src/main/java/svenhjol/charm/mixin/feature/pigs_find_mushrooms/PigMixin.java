package svenhjol.charm.mixin.feature.pigs_find_mushrooms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal {
    protected PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 10) {
            Resolve.feature(PigsFindMushrooms.class).handlers.animationTicks.put(getUUID(), 40);
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public void aiStep() {
        var handlers = Resolve.feature(PigsFindMushrooms.class).handlers;
        var uuid = getUUID();
        if (handlers.animationTicks.containsKey(uuid)) {
            handlers.animationTicks.compute(uuid, (k, ticks) -> ticks - 1);
        }
        super.aiStep();
    }
}
