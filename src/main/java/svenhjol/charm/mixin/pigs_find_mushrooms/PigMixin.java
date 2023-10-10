package svenhjol.charm.mixin.pigs_find_mushrooms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal {
    protected PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 10) {
            PigsFindMushrooms.PIG_ANIMATION_TICKS.put(getUUID(), 40);
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public void aiStep() {
        var uuid = getUUID();
        if (PigsFindMushrooms.PIG_ANIMATION_TICKS.containsKey(uuid)) {
            var ticks = PigsFindMushrooms.PIG_ANIMATION_TICKS.get(uuid);
            PigsFindMushrooms.PIG_ANIMATION_TICKS.put(uuid, ticks - 1);
        }
        super.aiStep();
    }
}
