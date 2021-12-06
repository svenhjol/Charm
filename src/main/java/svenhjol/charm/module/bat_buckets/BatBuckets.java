package svenhjol.charm.module.bat_buckets;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.helper.NbtHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.loader.CharmModule;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.")
public class BatBuckets extends CharmModule {
    private static final int PLAYER_CHECK_TICKS = 10;

    public static final ResourceLocation TRIGGER_CAPTURED_BAT = new ResourceLocation(Charm.MOD_ID, "captured_bat");
    public static final ResourceLocation TRIGGER_USED_BAT_BUCKET = new ResourceLocation(Charm.MOD_ID, "used_bat_bucket");

    public static final SoundEvent BAT_BUCKET_GRAB = CharmSounds.createSound("bat_bucket_grab");
    public static final SoundEvent BAT_BUCKET_RELEASE = CharmSounds.createSound("bat_bucket_release");

    public static BatBucketItem BAT_BUCKET_ITEM;
    public static EcholocationEffect ECHOLOCATION;

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int glowingTime = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int glowingRange = 24;

    @Config(name = "Damage bat", description = "If true, the bat will take half a heart of damage when released from the bucket.")
    public static boolean damageBat = true;

    @Override
    public void register() {
        BAT_BUCKET_ITEM = new BatBucketItem(this);
        ECHOLOCATION = new EcholocationEffect(this);
    }

    @Override
    public void runWhenEnabled() {
        UseEntityCallback.EVENT.register(this::tryCapture);
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide && player.level.getGameTime() % PLAYER_CHECK_TICKS == 0 && player.hasEffect(ECHOLOCATION)) {
            AABB box = player.getBoundingBox().inflate(BatBuckets.glowingRange, BatBuckets.glowingRange / 2.0, BatBuckets.glowingRange);
            MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, PLAYER_CHECK_TICKS);
            List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, box);

            for (LivingEntity entity : entities) {
                if (entity.getUUID().equals(player.getUUID())) continue;
                if (entity.canBeAffected(effect)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, PLAYER_CHECK_TICKS + 5, 0, false, true), player);
                }
            }
        }
    }

    private InteractionResult tryCapture(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (!entity.level.isClientSide && entity instanceof Bat bat && bat.getHealth() > 0) {
            ItemStack held = player.getItemInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET) {
                return InteractionResult.PASS;
            }

            ItemStack batBucket = new ItemStack(BAT_BUCKET_ITEM);
            CompoundTag tag = new CompoundTag();
            NbtHelper.setCompound(batBucket, BatBucketItem.STORED_BAT_NBT, bat.saveWithoutId(tag));

            if (held.getCount() == 1) {
                player.setItemInHand(hand, batBucket);
            } else {
                held.shrink(1);
                player.getInventory().placeItemBackInInventory(batBucket);
            }

            playGrabSound((ServerLevel)bat.level, bat.blockPosition());
            player.getCooldowns().addCooldown(BAT_BUCKET_ITEM, 30);
            player.swing(hand);
            entity.discard();

            triggerCapturedBat((ServerPlayer) player);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    public static void playGrabSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, BatBuckets.BAT_BUCKET_GRAB, SoundSource.PLAYERS, 0.6F, 0.95F + level.getRandom().nextFloat() * 0.2F);
    }

    public static void playReleaseSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, BatBuckets.BAT_BUCKET_RELEASE, SoundSource.PLAYERS, 0.6F, 0.95F + level.getRandom().nextFloat() * 0.2F);
    }

    public static void playLaunchSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BAT_TAKEOFF, SoundSource.PLAYERS, 0.25F, 1F);
    }

    public static void triggerCapturedBat(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_CAPTURED_BAT);
    }

    public static void triggerUsedBatBucket(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_BAT_BUCKET);
    }
}
