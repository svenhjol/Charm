package svenhjol.charm.module.coral_squids;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.module.coral_squids.CoralSquidBucketItem;
import svenhjol.charm.module.coral_squids.CoralSquids;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

/**
 * Most of this is copypasta from SquidEntity.
 * canSpawn() checks for coral.
 */
public class CoralSquidEntity extends WaterAnimal {
    public static final String CORAL_SQUID_TYPE_NBT = "CoralSquidType";
    public static final String CORAL_SQUID_FROM_BUCKET_NBT = "FromBucket";

    private static final EntityDataAccessor<Boolean> FROM_BUCKET;
    private static final EntityDataAccessor<Integer> CORAL_SQUID_TYPE;

    public static final Map<Integer, ResourceLocation> TEXTURES;
    public static final Map<Integer, Item> DROPS;

    public float tiltAngle;
    public float prevTiltAngle;
    public float rollAngle;
    public float prevRollAngle;
    public float thrustTimer;
    public float prevThrustTimer;
    public float tentacleAngle;
    public float prevTentacleAngle;
    private float swimVelocityScale;
    private float thrustTimerSpeed;
    private float turningSpeed;
    private float swimX;
    private float swimY;
    private float swimZ;

    public CoralSquidEntity(EntityType<? extends CoralSquidEntity> entityType, Level world) {
        super(entityType, world);
        this.random.setSeed(this.getId());
        this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.28F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
        setCoralSquidType(random.nextInt(5));
        return entityData;
    }

    public static boolean canSpawn(EntityType<CoralSquidEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        boolean coralBelow = false;

        for (int y = 0; y > -16; y--) {
            BlockPos downPos = pos.offset(0, y, 0);
            BlockState downState = world.getBlockState(downPos);
            coralBelow = downState.getBlock() instanceof BaseCoralPlantTypeBlock;

            if (coralBelow)
                break;
        }

        boolean canSpawn = pos.getY() > 20
            && pos.getY() < world.getSeaLevel()
            && coralBelow;

        if (canSpawn)
            Charm.LOG.debug("Can spawn coral squid at " + pos.toString());

        return canSpawn;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4; // might be important for performance
    }

    public ResourceLocation getTexture() {
        return TEXTURES.getOrDefault(getCoralSquidType(), TEXTURES.get(0));
    }

    public int getCoralSquidType() {
        return this.entityData.get(CORAL_SQUID_TYPE);
    }

    public void setCoralSquidType(int type) {
        if (type < 0 || type > 4)
            type = this.random.nextInt(5);

        this.entityData.set(CORAL_SQUID_TYPE, type);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CORAL_SQUID_TYPE, 1);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(CORAL_SQUID_TYPE_NBT, this.getCoralSquidType());
        nbt.putBoolean(CORAL_SQUID_FROM_BUCKET_NBT, this.isFromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setCoralSquidType(nbt.getInt(CORAL_SQUID_TYPE_NBT));
        this.setFromBucket(nbt.getBoolean(CORAL_SQUID_FROM_BUCKET_NBT));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CoralSquidEntity.EscapeAttackerGoal());
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropCustomDeathLoot(source, lootingMultiplier, allowDrops);
        Entity attacker = source.getEntity();

        if (attacker instanceof Player && random.nextFloat() < svenhjol.charm.module.coral_squids.CoralSquids.dropChance)
            this.spawnAtLocation(DROPS.get(getCoralSquidType()));
    }

    public static AttributeSupplier.Builder createSquidAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D);
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return 1.25F;
    }

    /**
     * Copypasta from {@link Squid#aiStep()}
     */
    public void aiStep() {
        super.aiStep();
        this.prevTiltAngle = this.tiltAngle;
        this.prevRollAngle = this.rollAngle;
        this.prevThrustTimer = this.thrustTimer;
        this.prevTentacleAngle = this.tentacleAngle;
        this.thrustTimer += this.thrustTimerSpeed;
        if ((double)this.thrustTimer > 6.283185307179586D) {
            if (this.level.isClientSide) {
                this.thrustTimer = 6.2831855F;
            } else {
                this.thrustTimer = (float)((double)this.thrustTimer - 6.283185307179586D);
                if (this.random.nextInt(7) == 0) {
                    this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.level.broadcastEntityEvent(this, (byte)19);
            }
        }

        if (this.isInWaterOrBubble()) {
            if (this.thrustTimer < 3.1415927F) {
                float f = this.thrustTimer / 3.1415927F;
                this.tentacleAngle = Mth.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double)f > 0.75D) {
                    this.swimVelocityScale = 1.0F;
                    this.turningSpeed = 1.0F;
                } else {
                    this.turningSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.swimVelocityScale *= 0.9F;
                this.turningSpeed *= 0.99F;
            }

            if (!this.level.isClientSide) {
                this.setDeltaMovement((double)(this.swimX * this.swimVelocityScale), (double)(this.swimY * this.swimVelocityScale), (double)(this.swimZ * this.swimVelocityScale));
            }

            Vec3 vec3d = this.getDeltaMovement();
            double d = vec3d.horizontalDistance();
            this.yBodyRot += (-((float)Mth.atan2(vec3d.x, vec3d.z)) * 57.295776F - this.yBodyRot) * 0.1F;
            this.setYRot(this.yBodyRot);
            this.rollAngle = (float)((double)this.rollAngle + 3.141592653589793D * (double)this.turningSpeed * 1.5D);
            this.tiltAngle += (-((float)Mth.atan2(d, vec3d.y)) * 57.295776F - this.tiltAngle) * 0.1F;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.thrustTimer)) * 3.1415927F * 0.25F;
            if (!this.level.isClientSide) {
                double d = this.getDeltaMovement().y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    d = 0.05D * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.isNoGravity()) {
                    d -= 0.08D;
                }

                this.setDeltaMovement(0.0D, d * 0.981D, 0.0D);
            }

            this.tiltAngle = (float)((double)this.tiltAngle + (double)(-90.0F - this.tiltAngle) * 0.02D);
        }

    }

    public void travel(Vec3 movementInput) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Environment(EnvType.CLIENT)
    public void handleEntityEvent(byte status) {
        if (status == 19) {
            this.thrustTimer = 0.0F;
        } else {
            super.handleEntityEvent(status);
        }
    }

    /**
     * Sets the direction and velocity the squid must go when fleeing an enemy. Only has an effect when in the water.
     */
    public void setSwimmingVector(float x, float y, float z) {
        this.swimX = x;
        this.swimY = y;
        this.swimZ = z;
    }

    public boolean hasSwimmingVector() {
        return this.swimX != 0.0F || this.swimY != 0.0F || this.swimZ != 0.0F;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        // copypasta from FishEntity
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() == Items.WATER_BUCKET && this.isAlive()) {
            this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
            held.shrink(1);

            ItemStack coralSquidBucket = new ItemStack(CoralSquids.CORAL_SQUID_BUCKET);
            CompoundTag nbt = new CompoundTag();
            ItemNBTHelper.setCompound(coralSquidBucket, CoralSquidBucketItem.STORED_CORAL_SQUID, this.saveWithoutId(nbt));

            if (this.hasCustomName())
                coralSquidBucket.setHoverName(this.getCustomName());

            if (!this.level.isClientSide)
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, coralSquidBucket);

            PlayerHelper.addOrDropStack(player, coralSquidBucket);

            this.discard();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    private boolean isFromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isFromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    class EscapeAttackerGoal extends Goal {
        private int timer;

        private EscapeAttackerGoal() {
        }

        public boolean canUse() {
            LivingEntity livingEntity = CoralSquidEntity.this.getLastHurtByMob();
            if (CoralSquidEntity.this.isInWater() && livingEntity != null) {
                return CoralSquidEntity.this.distanceToSqr(livingEntity) < 100.0D;
            } else {
                return false;
            }
        }

        public void start() {
            this.timer = 0;
        }

        public void tick() {
            ++this.timer;
            LivingEntity livingEntity = CoralSquidEntity.this.getLastHurtByMob();
            if (livingEntity != null) {
                Vec3 vec3d = new Vec3(CoralSquidEntity.this.getX() - livingEntity.getX(), CoralSquidEntity.this.getY() - livingEntity.getY(), CoralSquidEntity.this.getZ() - livingEntity.getZ());
                BlockState blockState = CoralSquidEntity.this.level.getBlockState(new BlockPos(CoralSquidEntity.this.getX() + vec3d.x, CoralSquidEntity.this.getY() + vec3d.y, CoralSquidEntity.this.getZ() + vec3d.z));
                FluidState fluidState = CoralSquidEntity.this.level.getFluidState(new BlockPos(CoralSquidEntity.this.getX() + vec3d.x, CoralSquidEntity.this.getY() + vec3d.y, CoralSquidEntity.this.getZ() + vec3d.z));
                if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
                    double d = vec3d.length();
                    if (d > 0.0D) {
                        vec3d.normalize();
                        float f = 3.0F;
                        if (d > 5.0D) {
                            f = (float)((double)f - (d - 5.0D) / 5.0D);
                        }

                        if (f > 0.0F) {
                            vec3d = vec3d.scale(f);
                        }
                    }

                    if (blockState.isAir()) {
                        vec3d = vec3d.subtract(0.0D, vec3d.y, 0.0D);
                    }

                    CoralSquidEntity.this.setSwimmingVector((float)vec3d.x / 20.0F, (float)vec3d.y / 20.0F, (float)vec3d.z / 20.0F);
                }

                if (this.timer % 10 == 5) {
                    CoralSquidEntity.this.level.addParticle(ParticleTypes.BUBBLE, CoralSquidEntity.this.getX(), CoralSquidEntity.this.getY(), CoralSquidEntity.this.getZ(), 0.0D, 0.0D, 0.0D);
                }

            }
        }
    }

    static class SwimGoal extends Goal {
        private final CoralSquidEntity squid;

        public SwimGoal(CoralSquidEntity squid) {
            this.squid = squid;
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            int i = this.squid.getNoActionTime();
            if (i > 100) {
                this.squid.setSwimmingVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRandom().nextInt(50) == 0 || !this.squid.wasTouchingWater || !this.squid.hasSwimmingVector()) {
                float f = this.squid.getRandom().nextFloat() * 6.2831855F;
                float g = Mth.cos(f) * 0.2F;
                float h = -0.1F + this.squid.getRandom().nextFloat() * 0.2F;
                float j = Mth.sin(f) * 0.2F;
                this.squid.setSwimmingVector(g, h, j);
            }
        }
    }

    static {
        CORAL_SQUID_TYPE = SynchedEntityData.defineId(CoralSquidEntity.class, EntityDataSerializers.INT);
        FROM_BUCKET = SynchedEntityData.defineId(CoralSquidEntity.class, EntityDataSerializers.BOOLEAN);

        TEXTURES = Util.make(Maps.newHashMap(), map -> {
            map.put(0, new ResourceLocation(Charm.MOD_ID, "textures/entity/coral_squid/tube.png"));
            map.put(1, new ResourceLocation(Charm.MOD_ID, "textures/entity/coral_squid/brain.png"));
            map.put(2, new ResourceLocation(Charm.MOD_ID, "textures/entity/coral_squid/bubble.png"));
            map.put(3, new ResourceLocation(Charm.MOD_ID, "textures/entity/coral_squid/fire.png"));
            map.put(4, new ResourceLocation(Charm.MOD_ID, "textures/entity/coral_squid/horn.png"));
        });
        DROPS = Util.make(Maps.newHashMap(), map -> {
            map.put(0, Items.TUBE_CORAL);
            map.put(1, Items.BRAIN_CORAL);
            map.put(2, Items.BUBBLE_CORAL);
            map.put(3, Items.FIRE_CORAL);
            map.put(4, Items.HORN_CORAL);
        });
    }
}
