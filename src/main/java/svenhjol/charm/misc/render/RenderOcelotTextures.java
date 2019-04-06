package svenhjol.charm.misc.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.misc.feature.RandomAnimalTextures;

public class RenderOcelotTextures extends RenderOcelot
{
    public RenderOcelotTextures(RenderManager manager)
    {
        super(manager);
    }

    public static IRenderFactory factory()
    {
        return RenderOcelotTextures::new;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityOcelot entity)
    {
        return RandomAnimalTextures.getOcelotTexture(entity);
    }
}
