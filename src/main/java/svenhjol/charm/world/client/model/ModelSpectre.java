package svenhjol.charm.world.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelSpectre extends ModelBase
{
    public ModelRenderer body;
    public ModelRenderer armr;
    public ModelRenderer arml;

    public ModelSpectre() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, -8.0F, -4.0F, 8, 24, 8, 0.0F);
        this.arml = new ModelRenderer(this, 32, 16);
        this.arml.mirror = true;
        this.arml.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.arml.addBox(-0.5F, -2.0F, -2.0F, 4, 12, 4, 0.0F); // positive makes them move away from the body
        this.armr = new ModelRenderer(this, 32, 16);
        this.armr.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armr.addBox(-3.5F, -2.0F, -2.0F, 4, 12, 4, 0.0F); // negative makes them move away from the body
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float r = 1F;
        float g = 1F;
        float b = 1F;
        GlStateManager.color(r, g, b);

        body.render(f5);
        arml.render(f5);
        armr.render(f5);
        GlStateManager.popMatrix();
    }
}
