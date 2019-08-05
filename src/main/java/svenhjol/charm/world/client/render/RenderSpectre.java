package svenhjol.charm.world.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.Charm;
import svenhjol.charm.world.client.layer.LayerSpectreEyes;
import svenhjol.charm.world.client.layer.LayerSpectreOutline;
import svenhjol.charm.world.client.model.ModelSpectre;
import svenhjol.charm.world.entity.EntitySpectre;

import javax.annotation.Nullable;

public class RenderSpectre extends RenderLiving<EntitySpectre>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/entity/spectre/spectre_base.png");
    public static final IRenderFactory FACTORY = RenderSpectre::new;

    public RenderSpectre(RenderManager manager)
    {
        super(manager, new ModelSpectre(), 0f);
        this.addLayer(new LayerSpectreOutline(this));
        this.addLayer(new LayerSpectreEyes(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntitySpectre entity)
    {
        return TEXTURE;
    }

    @Override
    protected float getDeathMaxRotation(EntitySpectre entityLivingBaseIn)
    {
        return 0.0F;
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
    {
        // don't
    }
}