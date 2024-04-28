package svenhjol.charm.feature.coral_squids;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

public class CoralSquidBucketItem extends MobBucketItem {
    public CoralSquidBucketItem() {
        super(CoralSquids.entity.get(),
            Fluids.WATER,
            SoundEvents.BUCKET_EMPTY_AXOLOTL,
            new Properties()
                .stacksTo(1)
                .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
    }
}
