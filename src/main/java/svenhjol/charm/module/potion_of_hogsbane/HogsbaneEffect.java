package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.potion.CharmStatusEffect;

public class HogsbaneEffect extends CharmStatusEffect {
    public HogsbaneEffect(CommonModule module) {
        super(module, "hogsbane", MobEffectCategory.BENEFICIAL, 0x20DFB8);
    }
}
