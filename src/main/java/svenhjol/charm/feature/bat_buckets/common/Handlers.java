package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.api.enums.ItemStackResult;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.common.helper.MobHelper;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.echolocation.Echolocation;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<BatBuckets> {
    private static final Echolocation ECHOLOCATION = Resolve.feature(Echolocation.class);
    private static final int GLOW_TIME = 10; // In seconds.

    public Handlers(BatBuckets feature) {
        super(feature);
    }

    public void playGrabSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, feature().registers.grabSound.get(),
            SoundSource.PLAYERS, 0.6f, 0.95f + level.getRandom().nextFloat() * 0.2f);
    }

    public void playReleaseSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, feature().registers.releaseSound.get(),
            SoundSource.PLAYERS, 0.6f, 0.95f + level.getRandom().nextFloat() * 0.2f);
    }

    public void playLaunchSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BAT_TAKEOFF,
            SoundSource.PLAYERS, 0.25f, 1f);
    }

    public ItemStackResult useItemInHand(Level level, Player player, InteractionHand hand) {
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
                playReleaseSound((ServerLevel)level, player.blockPosition());

                // Damage bat by half a heart.
                float health = mob.getHealth();
                mob.setHealth(health - 1.0F);
                if (mob.getHealth() > 0) {
                    playLaunchSound((ServerLevel)level, mob.blockPosition());
                }
            });
        }

        player.swing(hand);

        if (!player.level().isClientSide()) {
            feature().advancements.usedBatBucket(player);
            player.addEffect(new MobEffectInstance(ECHOLOCATION.registers.mobEffect.get(), GLOW_TIME * 20));
        }

        // Put empty bucket back in player's hand.
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }

        return ItemStackResult.consume(held);
    }

    @SuppressWarnings("unused")
    public InteractionResult useItemOnEntity(Player player, Level level, InteractionHand hand, Entity entity,
                                             @Nullable EntityHitResult hitResult) {
        if (!entity.level().isClientSide()
            && entity instanceof Bat bat
            && bat.getHealth() > 0
        ) {
            var held = player.getItemInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET) {
                return InteractionResult.PASS;
            }

            var result = useBucketOnBat(player, held, hand, bat);
            if (result) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean useBucketOnBat(Player player, ItemStack bucket, InteractionHand hand, Bat bat) {
        var batBucket = new ItemStack(feature().registers.bucketItem.get());
        var tag = new CompoundTag();
        bat.save(tag);
        batBucket.set(DataComponents.ENTITY_DATA, CustomData.of(tag));

        if (bucket.getCount() == 1) {
            player.setItemInHand(hand, batBucket);
        } else {
            bucket.shrink(1);
            player.getInventory().placeItemBackInInventory(batBucket);
        }

        playGrabSound((ServerLevel)bat.level(), bat.blockPosition());
        player.getCooldowns().addCooldown(feature().registers.bucketItem.get(), 30);
        player.swing(hand);
        bat.discard();

        feature().advancements.capturedBat(player);
        return true;
    }

}
