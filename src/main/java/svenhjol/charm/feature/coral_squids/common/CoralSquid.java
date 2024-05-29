package svenhjol.charm.feature.coral_squids.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.charmony.feature.FeatureResolver;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class CoralSquid extends WaterAnimal implements Bucketable, FeatureResolver<CoralSquids> {
    public static final String VARIANT_TAG = "Variant";
    public static final String CORAL_SQUID_FROM_BUCKET_TAG = "FromBucket";
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CoralSquid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(CoralSquid.class, EntityDataSerializers.INT);

    public float xBodyRot;
    public float xBodyRot0;
    public float zBodyRot;
    public float zBodyRot0;
    public float tentacleMovement;
    public float oldTentacleMovement;
    public float tentacleAngle;
    public float oldTentacleAngle;
    private float speed;
    private float tentacleSpeed;
    private float rotateSpeed;
    private float swimX;
    private float swimY;
    private float swimZ;

    public CoralSquid(EntityType<? extends CoralSquid> entityType, Level level) {
        super(entityType, level);
        this.random.setSeed(this.getId());
        this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2f;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData) {
        if (spawnType == MobSpawnType.BUCKET) {
            return groupData;
        }
        var random = level.getRandom();
        setVariant(Variant.randomly(random));
        return super.finalizeSpawn(level, difficulty, spawnType, groupData);
    }

    @SuppressWarnings("deprecation")
    public static boolean canSpawn(EntityType<CoralSquid> type, LevelAccessor level, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        var coralBelow = false;

        for (int y = 0; y > -16; y--) {
            BlockPos downPos = pos.offset(0, y, 0);
            BlockState downState = level.getBlockState(downPos);
            coralBelow = downState.getBlock() instanceof BaseCoralPlantTypeBlock;

            if (coralBelow) break;
        }

        return pos.getY() > 20
            && pos.getY() < level.getSeaLevel()
            && coralBelow;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4; // might be important for performance
    }

    public Variant getVariant() {
        return Variant.byId(entityData.get(DATA_VARIANT));
    }

    public void setVariant(Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.getId());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(VARIANT_TAG, getVariant().getId());
        nbt.putBoolean(CORAL_SQUID_FROM_BUCKET_TAG, this.isFromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(Variant.byId(nbt.getInt(VARIANT_TAG)));
        this.setFromBucket(nbt.getBoolean(CORAL_SQUID_FROM_BUCKET_TAG));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SquidRandomMovementGoal(this));
        this.goalSelector.addGoal(1, new SquidFleeGoal());
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean allowDrops) { // For 24w20a
        super.dropCustomDeathLoot(level, source, allowDrops);
        if (random.nextFloat() < feature().dropChance()) {
            this.spawnAtLocation(getVariant().getDrop());
        }
    }

    public static AttributeSupplier.Builder createSquidAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 5.0f);
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height() * 0.8f;
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
        return 1.25f;
    }

    /**
     * Copypasta from {@link Squid#aiStep()}
     */
    public void aiStep() {
        super.aiStep();
        this.xBodyRot0 = this.xBodyRot;
        this.zBodyRot0 = this.zBodyRot;
        this.oldTentacleMovement = this.tentacleMovement;
        this.oldTentacleAngle = this.tentacleAngle;
        this.tentacleMovement += this.tentacleSpeed;
        if ((double)this.tentacleMovement > Math.PI * 2) {
            if (this.level().isClientSide) {
                this.tentacleMovement = (float)Math.PI * 2;
            } else {
                this.tentacleMovement -= (float)Math.PI * 2;
                if (this.random.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0f / (this.random.nextFloat() + 1.0f) * 0.2f;
                }
                this.level().broadcastEntityEvent(this, (byte)19);
            }
        }
        if (this.isInWaterOrBubble()) {
            if (this.tentacleMovement < (float)Math.PI) {
                float f = this.tentacleMovement / (float)Math.PI;
                this.tentacleAngle = Mth.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25f;
                if ((double)f > 0.75) {
                    this.speed = 1.0f;
                    this.rotateSpeed = 1.0f;
                } else {
                    this.rotateSpeed *= 0.8f;
                }
            } else {
                this.tentacleAngle = 0.0f;
                this.speed *= 0.9f;
                this.rotateSpeed *= 0.99f;
            }
            if (!this.level().isClientSide) {
                this.setDeltaMovement(this.swimX * this.speed, this.swimY * this.speed, this.swimZ * this.speed);
            }
            Vec3 vec3 = this.getDeltaMovement();
            double d = vec3.horizontalDistance();
            this.yBodyRot += (-((float)Mth.atan2(vec3.x, vec3.z)) * 57.295776f - this.yBodyRot) * 0.1f;
            this.setYRot(this.yBodyRot);
            this.zBodyRot += (float)Math.PI * this.rotateSpeed * 1.5f;
            this.xBodyRot += (-((float)Mth.atan2(d, vec3.y)) * 57.295776f - this.xBodyRot) * 0.1f;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * (float)Math.PI * 0.25f;
            if (!this.level().isClientSide) {
                double e = this.getDeltaMovement().y;
                e = this.hasEffect(MobEffects.LEVITATION) ? 0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) : (e -= this.getGravity());
                this.setDeltaMovement(0.0, e * (double)0.98f, 0.0);
            }
            this.xBodyRot += (-90.0f - this.xBodyRot) * 0.02f;
        }
    }

    public void travel(Vec3 movementInput) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public void handleEntityEvent(byte status) {
        if (status == 19) {
            this.tentacleMovement = 0.0f;
        } else {
            super.handleEntityEvent(status);
        }
    }

    public void setMovementVector(float x, float y, float z) {
        this.swimX = x;
        this.swimY = y;
        this.swimZ = z;
    }

    public boolean hasMovementVector() {
        return this.swimX != 0.0f || this.swimY != 0.0f || this.swimZ != 0.0f;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this)
            .orElse(super.mobInteract(player, hand));
    }

    private boolean isFromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public boolean fromBucket() {
        return entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
            tag.putInt(VARIANT_TAG, getVariant().getId());
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadFromBucketTag(CompoundTag nbt) {
        Bucketable.loadDefaultDataFromBucketTag(this, nbt);
        setVariant(Variant.byId(nbt.getInt(VARIANT_TAG)));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(feature().registers.bucketItem.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return feature().registers.coralSquidBucketFill.get();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isFromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Override
    public Class<CoralSquids> typeForFeature() {
        return CoralSquids.class;
    }

    class SquidFleeGoal extends Goal {
        private int timer;

        private SquidFleeGoal() {
        }

        public boolean canUse() {
            var livingEntity = CoralSquid.this.getLastHurtByMob();
            if (CoralSquid.this.isInWater() && livingEntity != null) {
                return CoralSquid.this.distanceToSqr(livingEntity) < 100.0f;
            } else {
                return false;
            }
        }

        public void start() {
            this.timer = 0;
        }

        public void tick() {
            ++this.timer;
            var livingEntity = CoralSquid.this.getLastHurtByMob();
            if (livingEntity != null) {
                var vec3d = new Vec3(CoralSquid.this.getX() - livingEntity.getX(), CoralSquid.this.getY() - livingEntity.getY(), CoralSquid.this.getZ() - livingEntity.getZ());
                var blockState = CoralSquid.this.level().getBlockState(new BlockPos((int) (CoralSquid.this.getX() + vec3d.x), (int) (CoralSquid.this.getY() + vec3d.y), (int) (CoralSquid.this.getZ() + vec3d.z)));
                var fluidState = CoralSquid.this.level().getFluidState(new BlockPos((int) (CoralSquid.this.getX() + vec3d.x), (int) (CoralSquid.this.getY() + vec3d.y), (int) (CoralSquid.this.getZ() + vec3d.z)));
                if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
                    double d = vec3d.length();
                    if (d > 0.0d) {
                        vec3d.normalize();
                        float f = 3.0f;
                        if (d > 5.0d) {
                            f = (float)((double)f - (d - 5.0d) / 5.0d);
                        }

                        if (f > 0.0f) {
                            vec3d = vec3d.scale(f);
                        }
                    }

                    if (blockState.isAir()) {
                        vec3d = vec3d.subtract(0.0d, vec3d.y, 0.0d);
                    }

                    CoralSquid.this.setMovementVector((float)vec3d.x / 20.0f, (float)vec3d.y / 20.0f, (float)vec3d.z / 20.0f);
                }

                if (this.timer % 10 == 5) {
                    CoralSquid.this.level().addParticle(ParticleTypes.BUBBLE, CoralSquid.this.getX(), CoralSquid.this.getY(), CoralSquid.this.getZ(), 0.0d, 0.0d, 0.0d);
                }

            }
        }
    }

    private static class SquidRandomMovementGoal extends Goal {
        private final CoralSquid squid;

        public SquidRandomMovementGoal(CoralSquid squid) {
            this.squid = squid;
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            var i = this.squid.getNoActionTime();
            if (i > 100) {
                this.squid.setMovementVector(0.0f, 0.0f, 0.0f);
            } else if (this.squid.getRandom().nextInt(30) == 0 || !this.squid.wasTouchingWater || !this.squid.hasMovementVector()) {
                var f = this.squid.getRandom().nextFloat() * 6.2831855f;
                var g = Mth.cos(f) * 0.2f;
                var h = -0.1F + this.squid.getRandom().nextFloat() * 0.05f;
                var j = Mth.sin(f) * 0.2f;
                this.squid.setMovementVector(g, h, j);
            }
        }
    }
}
