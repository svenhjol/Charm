package svenhjol.charm.module;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.AstrolabesClient;
import svenhjol.charm.item.AstrolabeItem;

@Module(mod = Charm.MOD_ID, client = AstrolabesClient.class)
public class Astrolabes extends CharmModule {
    public static AstrolabeItem ASTROLABE;
    public static DefaultParticleType AXIS_PARTICLE;

    @Override
    public void register() {
        ASTROLABE = new AstrolabeItem(this);
        AXIS_PARTICLE = RegistryHandler.defaultParticleType(new Identifier(Charm.MOD_ID, "axis"));
    }
}
