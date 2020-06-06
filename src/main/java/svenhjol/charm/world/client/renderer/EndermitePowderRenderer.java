package svenhjol.charm.world.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.world.entity.EndermitePowderEntity;

public class EndermitePowderRenderer extends EntityRenderer<EndermitePowderEntity> {
    public EndermitePowderRenderer(EntityRendererManager render) {
        super(render);
    }

    @Override
    public ResourceLocation getEntityTexture(EndermitePowderEntity entity) {
        return null;
    }

}
