package svenhjol.charm.feature.stonger_anvils;

import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.Random;

@Feature(mod = Charm.MOD_ID, description = "Anvils are 50% less likely to take damage when used.")
public class StrongerAnvils extends CharmFeature {
    public static boolean tryDamageAnvil() {
        return new Random().nextFloat() < 0.5F;
    }
}
