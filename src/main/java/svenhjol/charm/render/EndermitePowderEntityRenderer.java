package svenhjol.charm.render;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import svenhjol.charm.entity.EndermitePowderEntity;

public class EndermitePowderEntityRenderer extends EntityRenderer<EndermitePowderEntity> {
    public EndermitePowderEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(EndermitePowderEntity entity) {
        return null;
    }
}
