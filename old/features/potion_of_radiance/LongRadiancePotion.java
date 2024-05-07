package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class LongRadiancePotion extends Potion {
    public LongRadiancePotion() {
        super(new MobEffectInstance(MobEffects.GLOWING, 9600));
    }
}
