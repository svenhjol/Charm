package svenhjol.charm.module.potion_of_spelunking;

import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.potion.CharmStatusEffect;

public class SpelunkingEffect extends CharmStatusEffect {
    public SpelunkingEffect(CharmCommonModule module) {
        super(module, "spelunking", MobEffectCategory.NEUTRAL, 0xC09FDD);
    }
}
