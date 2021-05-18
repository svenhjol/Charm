package svenhjol.charm.module;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Potion effect particles are much less obtrusive by default and can optionally be entirely hidden.",
    requiresMixins = {"gentle_potion_particles.*"})
public class GentlePotionParticles extends CharmModule {
    @Config(name = "Translucent particles", description = "If true, faded/translucent particles will be rendered.  If false, no particles will be rendered.")
    public static boolean translucent = true;

    public static boolean tryRenderParticles(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (!ModuleHandler.enabled(GentlePotionParticles.class))
            return false;

        if (translucent)
            world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y, z, velocityX, velocityY, velocityZ);

        return true;
    }
}
