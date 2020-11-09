package svenhjol.charm.model;

import net.minecraft.*;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class CoralSquidEntityModel<T extends Entity> extends class_5597<T> {
    private final ModelPart[] tentacles = new ModelPart[8];
    private final ModelPart modelPart;

    public CoralSquidEntityModel(ModelPart modelPart) {
        this.modelPart = modelPart;
        Arrays.setAll(this.tentacles, t -> modelPart.method_32086(getTentacleIndex(t)));
    }

    private static String getTentacleIndex(int i) {
        return "tentacle" + i;
    }

    public static class_5607 render() {
        class_5609 lv = new class_5609();
        class_5610 lv2 = lv.method_32111();
        lv2.method_32117("body", class_5606.method_32108().method_32101(0, 0).method_32097(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), class_5603.method_32090(0.0F, 4.0F, 0.0F));
        class_5606 lv3 = class_5606.method_32108().method_32101(48, 0).method_32097(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F);

        for(int k = 0; k < 8; ++k) {
            double d = (double)k * 3.141592653589793D * 2.0D / 8.0D;
            float f = (float)Math.cos(d) * 2.5F;
            float g = 7.5F;
            float h = (float)Math.sin(d) * 2.5F;
            d = (double)k * 3.141592653589793D * -2.0D / 8.0D + 1.5707963267948966D;
            float l = (float)d;
            lv2.method_32117(getTentacleIndex(k), lv3, class_5603.method_32091(f, g, h, 0.0F, l, 0.0F));
        }

        return class_5607.method_32110(lv, 64, 32);
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
    public ModelPart method_32008() {
        return this.modelPart;
    }
}
