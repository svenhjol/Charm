package svenhjol.charm.feature.stronger_anvils;

import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.Random;

@Feature(mod = Charm.MOD_ID, description = "Anvils are 50% less likely to take damage when used.")
public class StrongerAnvils extends CharmonyFeature {
    public static boolean tryDamageAnvil() {
        return new Random().nextFloat() < 0.5F;
    }
}
