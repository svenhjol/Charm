package svenhjol.charm.mixin.extra_stews;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.extra_stews.ExtraStews;

import java.util.List;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin implements SuspiciousEffectHolder {
    @Override
    public List<EffectEntry> getSuspiciousEffects() {
        if (isPitcherPlant()) {
            var duration = ExtraStews.getPitcherPlantEffectDuration();
            if (duration > 0) {
                return List.of(
                    new SuspiciousEffectHolder.EffectEntry(MobEffects.DAMAGE_BOOST, duration * 20),
                    new SuspiciousEffectHolder.EffectEntry(MobEffects.REGENERATION, duration * 20)
                );
            }
        }
        return List.of();
    }

    @Unique
    private boolean isPitcherPlant() {
        return (Object)this == Blocks.PITCHER_PLANT;
    }
}
