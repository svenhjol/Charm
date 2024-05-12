package svenhjol.charm.feature.coral_squids.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.foundation.Resolve;

public class BucketItem extends MobBucketItem {
    private static final CoralSquids CORAL_SQUIDS = Resolve.feature(CoralSquids.class);

    public BucketItem() {
        super(CORAL_SQUIDS.registers.entity.get(),
            Fluids.WATER,
            SoundEvents.BUCKET_EMPTY_AXOLOTL,
            new Properties()
                .stacksTo(1)
                .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
    }
}
