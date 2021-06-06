package svenhjol.charm.module.glowballs;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.event.ClientSpawnEntityCallback;
import svenhjol.charm.module.glowballs.GlowballEntity;
import svenhjol.charm.module.glowballs.Glowballs;

public class GlowballsClient extends CharmClientModule {
    public GlowballsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(svenhjol.charm.module.glowballs.Glowballs.GLOWBALL, dispatcher
            -> new ThrownItemRenderer<>(dispatcher, 1.0F, true));

        BlockRenderLayerMap.INSTANCE.putBlock(svenhjol.charm.module.glowballs.Glowballs.GLOWBALL_BLOCK, RenderType.translucent());
    }

    @Override
    public void init() {
        ClientSpawnEntityCallback.EVENT.register(this::handleClientSpawnEntity);
    }

    private void handleClientSpawnEntity(ClientboundAddEntityPacket packet, EntityType<?> entityType, ClientLevel world, double x, double y, double z) {
        if (entityType == Glowballs.GLOWBALL)
            ClientSpawnEntityCallback.addEntity(packet, world, new GlowballEntity(world, x, y, z));
    }
}
