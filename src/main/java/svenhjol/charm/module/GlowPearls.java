package svenhjol.charm.module;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.client.GlowPearlsClient;
import svenhjol.charm.entity.GlowPearlEntity;
import svenhjol.charm.item.GlowPearlItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Glow Pearls")
public class GlowPearls extends MesonModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "glow_pearl");
    public static GlowPearlItem GLOW_PEARL;
    public static EntityType<GlowPearlEntity> ENTITY;

    public static GlowPearlsClient client;

    @Override
    public void register() {
        GLOW_PEARL = new GlowPearlItem(this);

        ENTITY = EntityType.Builder.<GlowPearlEntity>create(GlowPearlEntity::new, SpawnGroup.MISC)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .setDimensions(0.25F, 0.25F)
            .build(ID.getPath());

        Registry.register(Registry.ENTITY_TYPE, ID, ENTITY);
    }

    @Override
    public void clientRegister() {
        client = new GlowPearlsClient(this);
    }
}
