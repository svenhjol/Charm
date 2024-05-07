package svenhjol.charm.feature.echolocation;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EcholocationStatusEffect extends MobEffect {
    protected EcholocationStatusEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x667766);
    }
}
