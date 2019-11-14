package svenhjol.charm.world.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import svenhjol.charm.Charm;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;

import java.nio.FloatBuffer;
import java.util.Random;

public class RunePortalTileEntityRenderer extends TileEntityRenderer<RunePortalTileEntity>
{
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation RUNE_PORTAL_TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/entity/rune_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public void render(RunePortalTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.disableLighting();

        RANDOM.setSeed(10L);
        GlStateManager.getMatrix(2982, MODELVIEW);
        GlStateManager.getMatrix(2983, PROJECTION);
        int i = 13;
        float f = this.getOffset();
        boolean flag = false;
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;

        for(int j = 0; j < i; ++j) {
            GlStateManager.pushMatrix();
            float f1 = 8.0F / (float)(30 - j);

            if (j == 0) {
                this.bindTexture(END_SKY_TEXTURE);
                f1 = 0.05F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }

            if (j >= 1) {
                this.bindTexture(RUNE_PORTAL_TEXTURE);
                gamerenderer.setupFogColor(true);
            }

            if (j == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            }

            GlStateManager.texGenMode(GlStateManager.TexGen.S, 9216);
            GlStateManager.texGenMode(GlStateManager.TexGen.T, 9216);
            GlStateManager.texGenMode(GlStateManager.TexGen.R, 9216);
            GlStateManager.texGenParam(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGenParam(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.enableTexGen(GlStateManager.TexGen.S);
            GlStateManager.enableTexGen(GlStateManager.TexGen.T);
            GlStateManager.enableTexGen(GlStateManager.TexGen.R);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translatef(0.25F, 0.25F, 0.0F);
            GlStateManager.scalef(0.4F, 0.4F, 1.0F);
            float f2 = (float)((j + 6) * 0.75F);
            GlStateManager.translatef(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)(Util.milliTime() % 200000L) / 200000.0F), 0.0F);
            GlStateManager.rotatef((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);

            GlStateManager.scalef(5.6F - f2 / 4.0F, 5.1F - f2 / 4.0F, 1.0F);

            GlStateManager.multMatrix(PROJECTION);
            GlStateManager.multMatrix(MODELVIEW);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            float r = 1.0F;
            float g = 1.0F;
            float b = 1.0F;

            int k = j-1;
            if (k >= 0 && tile.colors != null && k < tile.colors.size()) {
                int colorIndex = tile.colors.get(k);
                DyeColor dyeColor = DyeColor.byId(colorIndex);
                float[] comps = dyeColor.getColorComponentValues();
                r = comps[0];
                g = comps[1];
                b = comps[2];
            }

            r *= 1.7F * f1;
            g *= 1.7F * f1;
            b *= 1.7F * f1;

            // only render facing up
            bufferbuilder.pos(x, y + (double)f, z + 1.0D).color(r, g, b, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y + (double)f, z + 1.0D).color(r, g, b, 1.0F).endVertex();
            bufferbuilder.pos(x + 1.0D, y + (double)f, z).color(r, g, b, 1.0F).endVertex();
            bufferbuilder.pos(x, y + (double)f, z).color(r, g, b, 1.0F).endVertex();

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(END_SKY_TEXTURE);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableTexGen(GlStateManager.TexGen.S);
        GlStateManager.disableTexGen(GlStateManager.TexGen.T);
        GlStateManager.disableTexGen(GlStateManager.TexGen.R);
        GlStateManager.enableLighting();
        gamerenderer.setupFogColor(false);
    }

    protected float getOffset() {
        return 0.75F;
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
