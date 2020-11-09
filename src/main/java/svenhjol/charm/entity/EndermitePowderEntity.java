package svenhjol.charm.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import svenhjol.charm.module.EndermitePowder;

@SuppressWarnings("EntityConstructor")
public class EndermitePowderEntity extends Entity {
    public int ticks = 0;

    private static final TrackedData<Integer> TARGET_X = DataTracker.registerData(EndermitePowderEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_Z = DataTracker.registerData(EndermitePowderEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final String TAG_TARGET_X = "targetX";
    private static final String TAG_TARGET_Z = "targetZ";

    public EndermitePowderEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    public EndermitePowderEntity(World world, int x, int z) {
        this(EndermitePowder.ENTITY, world);
        dataTracker.set(TARGET_X, x);
        dataTracker.set(TARGET_Z, z);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(TARGET_X, 0);
        dataTracker.startTracking(TARGET_Z, 0);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        dataTracker.set(TARGET_X, tag.getInt(TAG_TARGET_X));
        dataTracker.set(TARGET_Z, tag.getInt(TAG_TARGET_Z));
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
        tag.putInt(TAG_TARGET_X, dataTracker.get(TARGET_X));
        tag.putInt(TAG_TARGET_Z, dataTracker.get(TARGET_Z));
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        double posSpread = 0.5;
        double scale = 0.2;
        double rise = 0.03;
        int maxLiveTime = 1000;
        int particles = 18;
        int x = getBlockPos().getX();
        int y = getBlockPos().getY();
        int z = getBlockPos().getZ();

        Vec3d vec = new Vec3d((double) dataTracker.get(TARGET_X), y, (double) dataTracker.get(TARGET_Z))
            .subtract(x, y, z).normalize().multiply(scale);

        double bpx = x + vec.x * ticks;
        double bpy = y + vec.y * ticks + ticks * rise;
        double bpz = z + vec.z * ticks;

        if (!world.isClient) {
            for (int i = 0; i < particles; i++) {
                double px = bpx + (Math.random() - 0.5) * posSpread;
                double py = bpy + (Math.random() - 0.5) * posSpread;
                double pz = bpz + (Math.random() - 0.5) * posSpread;
                ((ServerWorld) world).spawnParticles(ParticleTypes.PORTAL, px, py, pz, 1, 0.2D, 0.12D, 0.1D, 0.06D);
            }
        }

        if (ticks++ > maxLiveTime) {
            discard();
            ticks = 0;
        }
    }
}
