package svenhjol.charm.module.potion_of_spelunking;

import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.potion.CharmStatusEffect;

public class SpelunkingEffect extends CharmStatusEffect {
    public SpelunkingEffect(CommonModule module) {
        super(module, "spelunking", MobEffectCategory.NEUTRAL, 0xC09FDD);
    }
}
