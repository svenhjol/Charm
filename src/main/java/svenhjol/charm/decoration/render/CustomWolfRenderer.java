package svenhjol.charm.decoration.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.decoration.module.RandomAnimalTextures;

public class CustomWolfRenderer extends WolfRenderer {
    public CustomWolfRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @SuppressWarnings("rawtypes")
    public static IRenderFactory factory() {
        return CustomWolfRenderer::new;
    }

    @Override
    public ResourceLocation getEntityTexture(WolfEntity entity) {
        return RandomAnimalTextures.getWolfTexture(entity);
    }
}
