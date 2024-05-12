package svenhjol.charm.feature.echolocation.common;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Effect extends MobEffect {
    protected Effect() {
        super(MobEffectCategory.BENEFICIAL, 0x667766);
    }
}
