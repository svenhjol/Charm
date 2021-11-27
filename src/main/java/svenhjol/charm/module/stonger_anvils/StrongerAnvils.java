package svenhjol.charm.module.stonger_anvils;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Anvils are 50% less likely to take damage when used.")
public class StrongerAnvils extends CharmModule {
    public static boolean tryDamageAnvil() {
        return Charm.LOADER.isEnabled(StrongerAnvils.class) && new Random().nextFloat() < 0.5F;
    }
}
