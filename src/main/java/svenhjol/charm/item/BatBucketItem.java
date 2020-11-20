package svenhjol.charm.item;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.client.BatBucketsClient;
import svenhjol.charm.module.BatBuckets;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.item.CharmItem;

public class BatBucketItem extends CharmItem {
    public static final String STORED_BAT = "stored_bat";

    public BatBucketItem(CharmModule module) {
        super(module, "bat_bucket", new Item.Settings()
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

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 1.0F, 1.0F);

        if (!world.isClient && !player.isCreative()) {

            double x = pos.getX() + 0.5F + facing.getOffsetX();
            double y = pos.getY() + 0.25F + (world.random.nextFloat() / 2.0F) + facing.getOffsetY();
            double z = pos.getZ() + 0.5F + facing.getOffsetZ();
            BlockPos spawnPos = new BlockPos(x, y, z);

            // spawn the bat
            BatEntity bat = MobHelper.spawn(EntityType.BAT, (ServerWorld)world, spawnPos, SpawnReason.BUCKET);
            if (bat != null) {

                CompoundTag data = ItemNBTHelper.getCompound(held, STORED_BAT);
                if (!data.isEmpty())
                    bat.readCustomDataFromTag(data);

                world.spawnEntity(bat);

                // damage the bat :(
                float health = bat.getHealth();
                bat.setHealth(health - 1.0F);
            }
        }
        player.swingHand(hand);

        // send message to client to start glowing
        if (!world.isClient) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeDouble(BatBuckets.glowingRange);
            data.writeInt(BatBuckets.glowingTime);

            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, BatBucketsClient.MSG_CLIENT_SET_GLOWING, data);
        }

        if (!player.isCreative())
            player.setStackInHand(hand, new ItemStack(Items.BUCKET));

        return ActionResult.SUCCESS;
    }
}
