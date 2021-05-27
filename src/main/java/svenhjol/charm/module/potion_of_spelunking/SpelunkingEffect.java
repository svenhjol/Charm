package svenhjol.charm.module.potion_of_spelunking;

import net.minecraft.entity.effect.StatusEffectType;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmStatusEffect;

public class SpelunkingEffect extends CharmStatusEffect {
    public SpelunkingEffect(CharmModule module) {
        super(module, "spelunking", StatusEffectType.NEUTRAL, 0xC09FDD);
    }
}
