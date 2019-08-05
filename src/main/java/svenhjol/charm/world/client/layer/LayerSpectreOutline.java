package svenhjol.charm.world.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.world.client.render.RenderSpectre;
import svenhjol.charm.world.entity.EntitySpectre;

public class LayerSpectreOutline implements LayerRenderer<EntitySpectre>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Charm.MOD_ID,"textures/entity/spectre/spectre_skin.png");
    public final RenderSpectre spectreRenderer;

    public LayerSpectreOutline(RenderSpectre spectreRenderer)
    {
        this.spectreRenderer = spectreRenderer;
    }

    @Override
    public void doRenderLayer(EntitySpectre spectre, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.spectreRenderer.bindTexture(TEXTURE);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.spectreRenderer.getMainModel().render(spectre, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}
