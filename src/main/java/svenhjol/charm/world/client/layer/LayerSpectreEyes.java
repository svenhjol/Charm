package svenhjol.charm.world.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.world.client.render.RenderSpectre;
import svenhjol.charm.world.entity.EntitySpectre;

public class LayerSpectreEyes implements LayerRenderer<EntitySpectre>
{
    public static final ResourceLocation SPECTRE_EYES = new ResourceLocation("charm","textures/entity/spectre/spectre_eyes.png");
    public final RenderSpectre spectreRenderer;

    public LayerSpectreEyes(RenderSpectre spectreRenderer)
    {
        this.spectreRenderer = spectreRenderer;
    }

    @Override
    public void doRenderLayer(EntitySpectre spectre, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean blinking = (ageInTicks % 200 < 6);
        if (blinking) return;

        this.spectreRenderer.bindTexture(SPECTRE_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
//        GlStateManager.disableLighting();

        GlStateManager.depthMask(!spectre.isInvisible());

        int i = '\uf0f0';
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);

//        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.spectreRenderer.getMainModel().render(spectre, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);

        i = spectre.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.spectreRenderer.setLightmap(spectre);
//        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();

    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
