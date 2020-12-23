package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.charm.item.CoralSquidBucketItem;

@Module(mod = Charm.MOD_ID, description = "Transport a Coral Squid using a bucket.")
public class CoralSquidBuckets extends CharmModule {
    public static CoralSquidBucketItem CORAL_SQUID_BUCKET;

    @Override
    public void register() {
        CORAL_SQUID_BUCKET = new CoralSquidBucketItem(this);
    }

    @Override
    public void init() {
        UseEntityCallback.EVENT.register(this::tryCapture);
    }

    private ActionResult tryCapture(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (!entity.world.isClient
                && (entity instanceof CoralSquidEntity)
                && (((CoralSquidEntity) entity).getHealth() > 0)
        ) {
            CoralSquidEntity coral_squid = (CoralSquidEntity) entity;
            ItemStack held = player.getStackInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return ActionResult.PASS;

            ItemStack coralSquidBucket = new ItemStack(CORAL_SQUID_BUCKET);
            CompoundTag tag = new CompoundTag();
            ItemNBTHelper.setCompound(coralSquidBucket, CoralSquidBucketItem.STORED_CORAL_SQUID, coral_squid.toTag(tag));

            if (held.getCount() == 1) {
                player.setStackInHand(hand, coralSquidBucket);
            } else {
                held.decrement(1);
                PlayerHelper.addOrDropStack(player, coralSquidBucket);
            }

            player.swingHand(hand);
            entity.remove();
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
