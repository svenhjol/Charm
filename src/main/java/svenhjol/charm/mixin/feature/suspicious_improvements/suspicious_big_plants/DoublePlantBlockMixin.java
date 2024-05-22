package svenhjol.charm.mixin.feature.suspicious_improvements.suspicious_big_plants;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.suspicious_improvements.suspicious_big_plants.SuspiciousBigPlants;
import svenhjol.charm.foundation.Resolve;

import java.util.List;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin implements SuspiciousEffectHolder {
    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        if (isPitcherPlant()) {
            var duration = Resolve.feature(SuspiciousBigPlants.class).pitcherPlantEffectDuration();
            if (duration > 0) {
                var entries = List.of(
                    new SuspiciousStewEffects.Entry(MobEffects.DAMAGE_BOOST, duration * 20),
                    new SuspiciousStewEffects.Entry(MobEffects.REGENERATION, duration * 20)
                );
                return new SuspiciousStewEffects(entries);
            }
        }
        return SuspiciousStewEffects.EMPTY;
    }

    @Unique
    private boolean isPitcherPlant() {
        return (Object)this == Blocks.PITCHER_PLANT;
    }
}
