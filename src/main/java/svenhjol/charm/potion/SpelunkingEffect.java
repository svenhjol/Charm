package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffectType;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.potion.CharmStatusEffect;

public class SpelunkingEffect extends CharmStatusEffect {
    public SpelunkingEffect(CharmModule module) {
        super(module, "spelunking", StatusEffectType.NEUTRAL, 0x00AADD);
    }
}
