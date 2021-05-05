package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffectType;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.potion.CharmStatusEffect;

public class HogsbaneEffect extends CharmStatusEffect {
    public HogsbaneEffect(CharmModule module) {
        super(module, "hogsbane", StatusEffectType.BENEFICIAL, 0x20DFB8);
    }
}
