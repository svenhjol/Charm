package svenhjol.charm.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class CoralSquidEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    private final ModelPart[] tentacles = new ModelPart[8];
    private final ModelPart modelPart;

    public CoralSquidEntityModel(ModelPart modelPart) {
        this.modelPart = modelPart;
        Arrays.setAll(this.tentacles, t -> modelPart.getChild(getTentacleIndex(t)));
    }

    private static String getTentacleIndex(int i) {
        return "tentacle" + i;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        lv2.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), ModelTransform.pivot(0.0F, 4.0F, 0.0F));
        ModelPartBuilder lv3 = ModelPartBuilder.create().uv(48, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F);

        for(int k = 0; k < 8; ++k) {
            double d = (double)k * 3.141592653589793D * 2.0D / 8.0D;
            float f = (float)Math.cos(d) * 2.5F;
            float g = 7.5F;
            float h = (float)Math.sin(d) * 2.5F;
            d = (double)k * 3.141592653589793D * -2.0D / 8.0D + 1.5707963267948966D;
            float l = (float)d;
            lv2.addChild(getTentacleIndex(k), lv3, ModelTransform.of(f, g, h, 0.0F, l, 0.0F));
        }

        return TexturedModelData.of(lv, 64, 32);
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        ModelPart[] var7 = this.tentacles;
        int var8 = var7.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            ModelPart modelPart = var7[var9];
            modelPart.pitch = animationProgress;
        }

    }

    @Override
    public ModelPart getPart() {
        return this.modelPart;
    }
}
