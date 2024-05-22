package svenhjol.charm.feature.crop_feather_falling.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import svenhjol.charm.feature.crop_feather_falling.CropFeatherFalling;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.EnchantmentsHelper;

public final class Handlers extends FeatureHolder<CropFeatherFalling> {
    public Handlers(CropFeatherFalling feature) {
        super(feature);
    }

    public boolean hasFeatherFalling(Entity entity) {
        return entity instanceof LivingEntity livingEntity && EnchantmentsHelper.featherFallingLevel(livingEntity) > 0;
    }
}
