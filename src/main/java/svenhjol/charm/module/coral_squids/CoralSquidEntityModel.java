package svenhjol.charm.module.coral_squids;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import java.util.Arrays;

public class CoralSquidEntityModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart[] tentacles = new ModelPart[8];
    private final ModelPart root;

    public CoralSquidEntityModel(ModelPart root) {
        this.root = root;
        Arrays.setAll(this.tentacles, t -> root.getChild(createTentacleName(t)));
    }

    private static String createTentacleName(int i) {
        return "tentacle" + i;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offset(0.0F, 16.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F);

        for(int k = 0; k < 8; ++k) {
            double d = (double)k * 3.141592653589793D * 2.0D / 8.0D;
            float f = (float)Math.cos(d) * 2.5F;
            float h = (float)Math.sin(d) * 2.5F;
            d = (double)k * 3.141592653589793D * -2.0D / 8.0D + 1.5707963267948966D;
            float l = (float)d;
            partDefinition.addOrReplaceChild(createTentacleName(k), cubeListBuilder, PartPose.offsetAndRotation(f, 20.0F, h, 0.0F, l, 0.0F));
        }

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        ModelPart[] var7 = this.tentacles;
        int var8 = var7.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            ModelPart modelPart = var7[var9];
            modelPart.xRot = animationProgress;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
