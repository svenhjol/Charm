package svenhjol.charm.module;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.GlowBallsClient;
import svenhjol.charm.entity.GlowBallEntity;
import svenhjol.charm.item.GlowBallItem;

@Module(mod = Charm.MOD_ID, description = "Glow Balls can be thrown to produce a light source where they impact the ground.")
public class GlowBalls extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "glow_ball");
    public static GlowBallItem GLOW_BALL;
    public static EntityType<GlowBallEntity> ENTITY;

    public static GlowBallsClient client;

    @Override
    public void register() {
        GLOW_BALL = new GlowBallItem(this);

        ENTITY = RegistryHandler.entity(ID, EntityType.Builder.<GlowBallEntity>create(GlowBallEntity::new, SpawnGroup.MISC)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .setDimensions(0.25F, 0.25F)
            .build(ID.getPath()));

        this.enabled = ModuleHandler.enabled("charm:placeable_glowstone_dust");
    }

    @Override
    public void clientRegister() {
        client = new GlowBallsClient(this);
    }
}
