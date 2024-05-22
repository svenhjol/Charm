package svenhjol.charm.mixin.feature.suspicious_improvements.suspicious_big_plants;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.TallFlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.suspicious_improvements.suspicious_big_plants.SuspiciousBigPlants;
import svenhjol.charm.foundation.Resolve;

import java.util.List;

@Mixin(TallFlowerBlock.class)
public class TallFlowerBlockMixin implements SuspiciousEffectHolder {
    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        if (isSunflower()) {
            var duration = Resolve.feature(SuspiciousBigPlants.class).sunflowerEffectDuration();
            if (duration > 0) {
                var entries = List.of(
                    new SuspiciousStewEffects.Entry(MobEffects.HEALTH_BOOST, duration * 20)
                );
                return new SuspiciousStewEffects(entries);
            }
        }
        return SuspiciousStewEffects.EMPTY;
    }

    @Unique
    private boolean isSunflower() {
        return (Object)this == Blocks.SUNFLOWER;
    }
}
