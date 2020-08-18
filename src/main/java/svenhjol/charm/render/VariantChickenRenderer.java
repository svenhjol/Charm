package svenhjol.charm.render;

import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.VariantAnimalTextures;

public class VariantChickenRenderer extends ChickenEntityRenderer {
    public VariantChickenRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(ChickenEntity entity) {
        return VariantAnimalTextures.getChickenTexture(entity);
    }
}
