package svenhjol.charm.base;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.RegistryHandler;

public class CharmParticles {
    public static DefaultParticleType AXIS_PARTICLE;

    public static void init() {
        AXIS_PARTICLE = RegistryHandler.defaultParticleType(new Identifier(Charm.MOD_ID, "axis"));
    }
}
