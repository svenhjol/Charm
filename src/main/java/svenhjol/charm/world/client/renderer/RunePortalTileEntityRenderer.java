package svenhjol.charm.world.client.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.Charm;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;

import java.util.List;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class RunePortalTileEntityRenderer<T extends RunePortalTileEntity> extends TileEntityRenderer<T> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation RUNE_PORTAL_TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/entity/rune_portal.png");
    private static final List<RenderType> RENDER_TYPES = IntStream.range(0, 16).mapToObj((i) -> getEndPortal(i + 1)).collect(ImmutableList.toImmutableList());

    public RunePortalTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        int i = 13;
        float f = 0.75F;
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        this.renderCube(tile, f, 0.15F, 0, matrix4f, bufferIn.getBuffer(RENDER_TYPES.get(0)));

        for(int j = 1; j < i; ++j) {
            this.renderCube(tile, f, 2.0F / (float)(18 - j), j, matrix4f, bufferIn.getBuffer(RENDER_TYPES.get(j)));
        }
    }

    private void renderCube(T tile, float offset, float depth, int pass, Matrix4f matrix4f, IVertexBuilder builder) {
        float f;

        if (pass == 0) {
            f = 0.05F;
        } else {
            f = 9.2F / (float) (30 - pass);
        }

        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;

        if (pass >= 0 && tile.colors != null && pass < tile.colors.size()) {
            int colorIndex = tile.colors.get(pass);
            DyeColor dyeColor = DyeColor.byId(colorIndex);
            float[] comps = dyeColor.getColorComponentValues();
            r = comps[0];
            g = comps[1];
            b = comps[2];
        }

        r *= 1.7F * f;
        g *= 1.7F * f;
        b *= 1.7F * f;

        this.renderFace(tile, matrix4f, builder, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, r, g, b, Direction.SOUTH);
        this.renderFace(tile, matrix4f, builder, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, r, g, b, Direction.NORTH);
        this.renderFace(tile, matrix4f, builder, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, r, g, b, Direction.EAST);
        this.renderFace(tile, matrix4f, builder, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, r, g, b, Direction.WEST);
        this.renderFace(tile, matrix4f, builder, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, r, g, b, Direction.DOWN);
        this.renderFace(tile, matrix4f, builder, 0.0F, 1.0F, offset, offset, 1.0F, 1.0F, 0.0F, 0.0F, r, g, b, Direction.UP);
    }

    private void renderFace(T tileEntityIn, Matrix4f matrix4f, IVertexBuilder builder, float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float red, float green, float blue, Direction direction) {
        if (tileEntityIn.shouldRenderFace(direction)) {
            builder.pos(matrix4f, f0, f2, f4).color(red, green, blue, 1.0F).endVertex();
            builder.pos(matrix4f, f1, f2, f5).color(red, green, blue, 1.0F).endVertex();
            builder.pos(matrix4f, f1, f3, f6).color(red, green, blue, 1.0F).endVertex();
            builder.pos(matrix4f, f0, f3, f7).color(red, green, blue, 1.0F).endVertex();
        }
    }

    public static RenderType getEndPortal(int iterationIn) {
        RenderState.TransparencyState renderstate$transparencystate;
        RenderState.TextureState renderstate$texturestate;
        if (iterationIn <= 1) {
            renderstate$transparencystate = RenderType.TRANSLUCENT_TRANSPARENCY;
            renderstate$texturestate = new RenderState.TextureState(END_SKY_TEXTURE, false, false);
        } else {
            renderstate$transparencystate = RenderType.ADDITIVE_TRANSPARENCY;
            renderstate$texturestate = new RenderState.TextureState(RUNE_PORTAL_TEXTURE, false, false);
        }

        return RenderType.makeType("rune_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true,
            RenderType.State.getBuilder()
                .transparency(renderstate$transparencystate)
                .texture(renderstate$texturestate)
                .texturing(new RenderState.PortalTexturingState(iterationIn))
                .fog(RenderType.BLACK_FOG)
                .build(false));
    }
}
