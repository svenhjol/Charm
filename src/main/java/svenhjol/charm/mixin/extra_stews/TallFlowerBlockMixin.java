package svenhjol.charm.mixin.extra_stews;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.TallFlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.extra_stews.ExtraStews;

import java.util.List;

@Mixin(TallFlowerBlock.class)
public class TallFlowerBlockMixin implements SuspiciousEffectHolder {
    @Override
    public List<EffectEntry> getSuspiciousEffects() {
        if (isSunflower()) {
            var duration = ExtraStews.getSunflowerEffectDuration();
            if (duration > 0) {
                return List.of(
                    new SuspiciousEffectHolder.EffectEntry(MobEffects.HEALTH_BOOST, duration * 20)
                );
            }
        }
        return List.of();
    }

    @Unique
    private boolean isSunflower() {
        return (Object)this == Blocks.SUNFLOWER;
    }
}
