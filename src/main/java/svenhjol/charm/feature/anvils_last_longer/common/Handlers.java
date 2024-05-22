package svenhjol.charm.feature.anvils_last_longer.common;

import svenhjol.charm.feature.anvils_last_longer.AnvilsLastLonger;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.Random;

public final class Handlers extends FeatureHolder<AnvilsLastLonger> {
    public Handlers(AnvilsLastLonger feature) {
        super(feature);
    }

    public boolean tryDamageAnvil() {
        var chance = feature().chanceToDamage();
        var num = new Random().nextDouble();
        var willDamage = num < chance;
        feature().log().dev("Damage check " + (willDamage ? "passed" : "did not pass") + ": " + num + " < " + chance);
        return willDamage;
    }
}
