package svenhjol.charm.feature.stronger_anvils;

import svenhjol.charmony.common.CommonFeature;

import java.util.Random;

public class StrongerAnvils extends CommonFeature {
    @Override
    public String description() {
        return "Anvils are 50% less likely to take damage when used.";
    }

    public static boolean tryDamageAnvil() {
        return new Random().nextFloat() < 0.5f;
    }
}
