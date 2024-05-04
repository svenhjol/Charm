package svenhjol.charm.feature.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.foundation.helper.MobHelper;

public class BatBucketItem extends Item {
    public BatBucketItem() {
        super(new Properties().stacksTo(1));
    }

    public static boolean useBucketOnBat(Player player, ItemStack bucket, InteractionHand hand, Bat bat) {
        var batBucket = new ItemStack(BatBuckets.bucketItem.get());
        var tag = new CompoundTag();
        bat.save(tag);
        batBucket.set(DataComponents.ENTITY_DATA, CustomData.of(tag));

        if (bucket.getCount() == 1) {
            player.setItemInHand(hand, batBucket);
        } else {
            bucket.shrink(1);
            player.getInventory().placeItemBackInInventory(batBucket);
        }

        BatBuckets.playGrabSound((ServerLevel)bat.level(), bat.blockPosition());
        player.getCooldowns().addCooldown(BatBuckets.bucketItem.get(), 30);
        player.swing(hand);
        bat.discard();

        BatBuckets.triggerCapturedBat(player);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        var pos = player.blockPosition();
        var facing = player.getDirection();

        if (!level.isClientSide() && !player.getAbilities().instabuild) {
            var x = pos.getX() + 0.5d + facing.getStepX();
            var y = pos.getY() + 0.65d + (level.random.nextDouble() / 2.0d) + facing.getStepY();
            var z = pos.getZ() + 0.5d + facing.getStepZ();
            var spawnPos = new BlockPos((int) x, (int) y, (int) z);

            // Spawn the bat.
            MobHelper.spawn(EntityType.BAT, (ServerLevel)level, spawnPos, MobSpawnType.BUCKET, mob -> {
                var data = held.get(DataComponents.ENTITY_DATA);
                if (data != null) {
                    mob.readAdditionalSaveData(data.copyTag());
                }
                BatBuckets.playReleaseSound((ServerLevel)level, player.blockPosition());

                // Damage bat by half a heart.
                float health = mob.getHealth();
                mob.setHealth(health - 1.0F);
                if (mob.getHealth() > 0) {
                    BatBuckets.playLaunchSound((ServerLevel)level, mob.blockPosition());
                }
            });
        }

        player.swing(hand);

        if (!player.level().isClientSide()) {
            BatBuckets.triggerUsedBatBucket(player);
            player.addEffect(new MobEffectInstance(Echolocation.mobEffect.get(), BatBuckets.GLOW_TIME * 20));
        }

        // Put empty bucket back in player's hand.
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }

        return InteractionResultHolder.consume(held);
    }
}
