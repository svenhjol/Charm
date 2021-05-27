package svenhjol.charm.init;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.RegistryHelper;

public class CharmParticles {
    public static DefaultParticleType APPLY_PARTICLE;
    public static DefaultParticleType AXIS_PARTICLE;
    public static DefaultParticleType ORE_GLOW_PARTICLE;

    public static void init() {
        APPLY_PARTICLE = RegistryHelper.defaultParticleType(new Identifier(Charm.MOD_ID, "apply"));
        AXIS_PARTICLE = RegistryHelper.defaultParticleType(new Identifier(Charm.MOD_ID, "axis"));
        ORE_GLOW_PARTICLE = RegistryHelper.defaultParticleType(new Identifier(Charm.MOD_ID, "ore_glow"));
    }
}
