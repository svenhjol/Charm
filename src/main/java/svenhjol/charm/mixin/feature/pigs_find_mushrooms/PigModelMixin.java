package svenhjol.charm.mixin.feature.pigs_find_mushrooms;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;

/**
 * Extend the pig model with additional animation methods.
 * @param <T>
 */
@Mixin(PigModel.class)
public abstract class PigModelMixin<T extends Pig> extends QuadrupedModel<T> {
    @Unique
    private float headXRot;

    // Copypasta from PigModel
    protected PigModelMixin(ModelPart modelPart) {
        super(modelPart, false, 4.0f, 4.0f, 2.0f, 2.0f, 24);
    }

    @Override
    public void prepareMobModel(T pig, float f, float g, float h) {
        super.prepareMobModel(pig, f, g, h);
        var handlers = Resolve.feature(PigsFindMushrooms.class).handlers;
        this.head.y = 12.0f + handlers.getHeadEatPositionScale(pig, h) * 6.0f;
        this.headXRot = handlers.getHeadEatAngleScale(pig, h);
    }

    @Override
    public void setupAnim(T pig, float f, float g, float h, float i, float j) {
        super.setupAnim(pig, f, g, h, i, j);
        this.head.xRot = this.headXRot;
    }
}
