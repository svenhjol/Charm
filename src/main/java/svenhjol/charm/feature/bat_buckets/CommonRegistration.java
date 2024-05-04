package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.foundation.Registration;

import javax.annotation.Nullable;

public final class CommonRegistration extends Registration<BatBuckets> {
    public CommonRegistration(BatBuckets feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        BatBuckets.bucketItem = registry.item("bat_bucket", BatBucketItem::new);
        BatBuckets.grabSound = registry.soundEvent("bat_bucket_grab");
        BatBuckets.releaseSound = registry.soundEvent("bat_bucket_release");
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(this::handleEntityUse);
    }

    private InteractionResult handleEntityUse(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!entity.level().isClientSide()
            && entity instanceof Bat bat
            && bat.getHealth() > 0
        ) {
            var held = player.getItemInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET) {
                return InteractionResult.PASS;
            }

            var result = BatBucketItem.useBucketOnBat(player, held, hand, bat);
            if (result) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}
