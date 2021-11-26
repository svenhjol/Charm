package svenhjol.charm.module.bat_buckets;

import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.potion.CharmStatusEffect;

public class EcholocationEffect extends CharmStatusEffect {
    protected EcholocationEffect(CharmModule module) {
        super(module, "echolocation", MobEffectCategory.BENEFICIAL, 0x667766);
    }
}
