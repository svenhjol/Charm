package svenhjol.charm.tweaks.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.tweaks.feature.RandomAnimalTextures;

public class RenderWolfTextures extends RenderWolf
{
    public RenderWolfTextures(RenderManager manager)
    {
        super(manager);
    }

    public static IRenderFactory factory()
    {
        return RenderWolfTextures::new;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWolf entity)
    {
        return RandomAnimalTextures.getWolfTexture(entity);
    }
}
