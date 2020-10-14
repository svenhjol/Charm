package svenhjol.charm.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import svenhjol.meson.Meson;

public class CoralSquidEntity extends WaterCreatureEntity {
    public float tiltAngle;
    public float prevTiltAngle;
    public float rollAngle;
    public float prevRollAngle;
    /**
     * Timer between thrusts as the squid swims. Represented as an angle from 0 to 2PI.
     */
    public float thrustTimer;
    /**
     * This serves no real purpose.
     */
    public float prevThrustTimer;
    public float tentacleAngle;
    public float prevTentacleAngle;
    /**
     * A scale factor for the squid's swimming speed.
     *
     * Gets reset to 1 at the beginning of each thrust and gradually decreases to make the squid lurch around.
     */
    private float swimVelocityScale;
    private float thrustTimerSpeed;
    private float turningSpeed;
    private float swimX;
    private float swimY;
    private float swimZ;

    public CoralSquidEntity(EntityType<? extends CoralSquidEntity> entityType, World world) {
        super(entityType, world);
        this.random.setSeed(this.getEntityId());
        this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.4F;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        Box box = this.getBoundingBox().expand(0, 20, 0);

        BlockPos pos1 = new BlockPos(box.minX, box.minY, box.minZ);
        BlockPos pos2 = new BlockPos(box.maxX, box.maxY, box.maxZ);

        boolean canSpawn = BlockPos.stream(pos1, pos2).anyMatch(p -> {
            BlockState state = world.getBlockState(p);
            return state.getBlock() instanceof CoralBlock;
        });

        if (canSpawn)
            Meson.LOG.info("Can spawn at " + getBlockPos().toShortString());

        return canSpawn;
    }

    protected void initGoals() {
        this.goalSelector.add(0, new CoralSquidEntity.SwimGoal(this));
        this.goalSelector.add(1, new CoralSquidEntity.EscapeAttackerGoal());
    }

    public static DefaultAttributeContainer.Builder createSquidAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D);
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected boolean canClimb() {
        return false;
    }

    public void tickMovement() {
        super.tickMovement();
        this.prevTiltAngle = this.tiltAngle;
        this.prevRollAngle = this.rollAngle;
        this.prevThrustTimer = this.thrustTimer;
        this.prevTentacleAngle = this.tentacleAngle;
        this.thrustTimer += this.thrustTimerSpeed;
        if ((double)this.thrustTimer > 6.283185307179586D) {
            if (this.world.isClient) {
                this.thrustTimer = 6.2831855F;
            } else {
                this.thrustTimer = (float)((double)this.thrustTimer - 6.283185307179586D);
                if (this.random.nextInt(7) == 0) {
                    this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.world.sendEntityStatus(this, (byte)19);
            }
        }

        if (this.isInsideWaterOrBubbleColumn()) {
            if (this.thrustTimer < 3.1415927F) {
                float f = this.thrustTimer / 3.1415927F;
                this.tentacleAngle = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
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

            if (!this.world.isClient) {
                this.setVelocity((double)(this.swimX * this.swimVelocityScale), (double)(this.swimY * this.swimVelocityScale), (double)(this.swimZ * this.swimVelocityScale));
            }

            Vec3d vec3d = this.getVelocity();
            float g = MathHelper.sqrt(squaredHorizontalLength(vec3d));
            this.bodyYaw += (-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F - this.bodyYaw) * 0.1F;
            this.yaw = this.bodyYaw;
            this.rollAngle = (float)((double)this.rollAngle + 3.141592653589793D * (double)this.turningSpeed * 1.5D);
            this.tiltAngle += (-((float)MathHelper.atan2((double)g, vec3d.y)) * 57.295776F - this.tiltAngle) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.thrustTimer)) * 3.1415927F * 0.25F;
            if (!this.world.isClient) {
                double d = this.getVelocity().y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    d = 0.05D * (double)(this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    d -= 0.08D;
                }

                this.setVelocity(0.0D, d * 0.9800000190734863D, 0.0D);
            }

            this.tiltAngle = (float)((double)this.tiltAngle + (double)(-90.0F - this.tiltAngle) * 0.02D);
        }

    }

    public void travel(Vec3d movementInput) {
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 19) {
            this.thrustTimer = 0.0F;
        } else {
            super.handleStatus(status);
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

    class EscapeAttackerGoal extends Goal {
        private int timer;

        private EscapeAttackerGoal() {
        }

        public boolean canStart() {
            LivingEntity livingEntity = CoralSquidEntity.this.getAttacker();
            if (CoralSquidEntity.this.isTouchingWater() && livingEntity != null) {
                return CoralSquidEntity.this.squaredDistanceTo(livingEntity) < 100.0D;
            } else {
                return false;
            }
        }

        public void start() {
            this.timer = 0;
        }

        public void tick() {
            ++this.timer;
            LivingEntity livingEntity = CoralSquidEntity.this.getAttacker();
            if (livingEntity != null) {
                Vec3d vec3d = new Vec3d(CoralSquidEntity.this.getX() - livingEntity.getX(), CoralSquidEntity.this.getY() - livingEntity.getY(), CoralSquidEntity.this.getZ() - livingEntity.getZ());
                BlockState blockState = CoralSquidEntity.this.world.getBlockState(new BlockPos(CoralSquidEntity.this.getX() + vec3d.x, CoralSquidEntity.this.getY() + vec3d.y, CoralSquidEntity.this.getZ() + vec3d.z));
                FluidState fluidState = CoralSquidEntity.this.world.getFluidState(new BlockPos(CoralSquidEntity.this.getX() + vec3d.x, CoralSquidEntity.this.getY() + vec3d.y, CoralSquidEntity.this.getZ() + vec3d.z));
                if (fluidState.isIn(FluidTags.WATER) || blockState.isAir()) {
                    double d = vec3d.length();
                    if (d > 0.0D) {
                        vec3d.normalize();
                        float f = 3.0F;
                        if (d > 5.0D) {
                            f = (float)((double)f - (d - 5.0D) / 5.0D);
                        }

                        if (f > 0.0F) {
                            vec3d = vec3d.multiply((double)f);
                        }
                    }

                    if (blockState.isAir()) {
                        vec3d = vec3d.subtract(0.0D, vec3d.y, 0.0D);
                    }

                    CoralSquidEntity.this.setSwimmingVector((float)vec3d.x / 20.0F, (float)vec3d.y / 20.0F, (float)vec3d.z / 20.0F);
                }

                if (this.timer % 10 == 5) {
                    CoralSquidEntity.this.world.addParticle(ParticleTypes.BUBBLE, CoralSquidEntity.this.getX(), CoralSquidEntity.this.getY(), CoralSquidEntity.this.getZ(), 0.0D, 0.0D, 0.0D);
                }

            }
        }
    }

    class SwimGoal extends Goal {
        private final CoralSquidEntity squid;

        public SwimGoal(CoralSquidEntity squid) {
            this.squid = squid;
        }

        public boolean canStart() {
            return true;
        }

        public void tick() {
            int i = this.squid.getDespawnCounter();
            if (i > 100) {
                this.squid.setSwimmingVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRandom().nextInt(50) == 0 || !this.squid.touchingWater || !this.squid.hasSwimmingVector()) {
                float f = this.squid.getRandom().nextFloat() * 6.2831855F;
                float g = MathHelper.cos(f) * 0.2F;
                float h = -0.1F + this.squid.getRandom().nextFloat() * 0.2F;
                float j = MathHelper.sin(f) * 0.2F;
                this.squid.setSwimmingVector(g, h, j);
            }

        }
    }
}
