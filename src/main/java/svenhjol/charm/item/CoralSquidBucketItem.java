package svenhjol.charm.item;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.charm.module.CoralSquids;

import static svenhjol.charm.module.CoralSquids.CORAL_SQUID;

public class CoralSquidBucketItem extends CharmItem {
    public static final String STORED_CORAL_SQUID = "stored_coral_squid";

    public CoralSquidBucketItem(CharmModule module) {
        super(module, "coral_squid_bucket", new Item.Settings()
                .group(ItemGroup.MISC)
                .maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null || context.getWorld().isClient)
            return ActionResult.FAIL;

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction facing = context.getSide();
        Hand hand = context.getHand();
        ItemStack held = player.getStackInHand(hand);

        world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.25F);

        if (!world.isClient && !player.isCreative()) {
            double x = pos.getX() + 0.5F + facing.getOffsetX();
            double y = pos.getY() + 0.25F + (world.random.nextFloat() / 2.0F) + facing.getOffsetY();
            double z = pos.getZ() + 0.5F + facing.getOffsetZ();
            BlockPos spawnPos = new BlockPos(x, y, z);

            // spawn the coral squid

            if (CORAL_SQUID != null) {
                CoralSquidEntity coral_squid = MobHelper.spawn(CoralSquids.CORAL_SQUID, (ServerWorld)world, spawnPos, SpawnReason.BUCKET);
                CompoundTag data = ItemNBTHelper.getCompound(held, STORED_CORAL_SQUID);
                if (!data.isEmpty())
                    coral_squid.readCustomDataFromTag(data);

                world.spawnEntity(coral_squid);
            }
        }
        player.swingHand(hand);

        if (!player.isCreative())
            player.setStackInHand(hand, new ItemStack(Items.BUCKET));

        return ActionResult.SUCCESS;
    }
}
