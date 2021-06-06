package svenhjol.charm.module.endermite_powder;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.module.endermite_powder.EndermitePowder;

@SuppressWarnings("EntityConstructor")
public class EndermitePowderEntity extends Entity {
    public int ticks = 0;

    private static final EntityDataAccessor<Integer> TARGET_X = SynchedEntityData.defineId(EndermitePowderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_Z = SynchedEntityData.defineId(EndermitePowderEntity.class, EntityDataSerializers.INT);
    private static final String TARGET_X_NBT = "targetX";
    private static final String TARGET_Z_NBT = "targetZ";

    public EndermitePowderEntity(EntityType<? extends EndermitePowderEntity> type, Level world) {
        super(type, world);
    }

    public EndermitePowderEntity(Level world, int x, int z) {
        this(EndermitePowder.ENTITY, world);
        entityData.set(TARGET_X, x);
        entityData.set(TARGET_Z, z);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TARGET_X, 0);
        entityData.define(TARGET_Z, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        entityData.set(TARGET_X, nbt.getInt(TARGET_X_NBT));
        entityData.set(TARGET_Z, nbt.getInt(TARGET_Z_NBT));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt(TARGET_X_NBT, entityData.get(TARGET_X));
        nbt.putInt(TARGET_Z_NBT, entityData.get(TARGET_Z));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        double posSpread = 0.5;
        double scale = 0.2;
        double rise = 0.03;
        int maxLiveTime = 1000;
        int particles = 18;
        int x = blockPosition().getX();
        int y = blockPosition().getY();
        int z = blockPosition().getZ();

        Vec3 vec = new Vec3((double) entityData.get(TARGET_X), y, (double) entityData.get(TARGET_Z))
            .subtract(x, y, z).normalize().scale(scale);

        double bpx = x + vec.x * ticks;
        double bpy = y + vec.y * ticks + ticks * rise;
        double bpz = z + vec.z * ticks;

        if (!level.isClientSide) {
            for (int i = 0; i < particles; i++) {
                double px = bpx + (Math.random() - 0.5) * posSpread;
                double py = bpy + (Math.random() - 0.5) * posSpread;
                double pz = bpz + (Math.random() - 0.5) * posSpread;
                ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, px, py, pz, 1, 0.2D, 0.12D, 0.1D, 0.06D);
            }
        }

        if (ticks++ > maxLiveTime) {
            discard();
            ticks = 0;
        }
    }
}
