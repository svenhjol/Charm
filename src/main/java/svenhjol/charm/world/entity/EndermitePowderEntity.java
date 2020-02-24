package svenhjol.charm.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.world.module.EndermitePowder;

public class EndermitePowderEntity extends Entity
{
    public int ticks = 0;

    private static final DataParameter<Integer> TARGET_X = EntityDataManager.createKey(EndermitePowderEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TARGET_Z = EntityDataManager.createKey(EndermitePowderEntity.class, DataSerializers.VARINT);
    private static final String TAG_TARGET_X = "targetX";
    private static final String TAG_TARGET_Z = "targetZ";

    public EndermitePowderEntity(EntityType<? extends Entity> type, World world)
    {
        super(type, world);
    }

    public EndermitePowderEntity(World world, int x, int z)
    {
        this(EndermitePowder.entity, world);
        dataManager.set(TARGET_X, x);
        dataManager.set(TARGET_Z, z);
    }

    @Override
    protected void registerData()
    {
        dataManager.register(TARGET_X, 0);
        dataManager.register(TARGET_Z, 0);
    }

    @Override
    protected void readAdditional(CompoundNBT tag)
    {
        dataManager.set(TARGET_X, tag.getInt(TAG_TARGET_X));
        dataManager.set(TARGET_Z, tag.getInt(TAG_TARGET_Z));
    }

    @Override
    protected void writeAdditional(CompoundNBT tag)
    {
        tag.putInt(TAG_TARGET_X, dataManager.get(TARGET_X));
        tag.putInt(TAG_TARGET_Z, dataManager.get(TARGET_Z));
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void tick()
    {
        super.tick();

        double posSpread = 0.5;
        double scale = 0.2;
        double rise = 0.03;
        int maxLiveTime = 1000;
        int particles = 18;
        int x = getPosition().getX();
        int y = getPosition().getY();
        int z = getPosition().getZ();

        Vec3d vec = new Vec3d((double) dataManager.get(TARGET_X), y, (double) dataManager.get(TARGET_Z))
            .subtract(x, y, z).normalize().scale(scale);

        double bpx = x + vec.x * ticks;
        double bpy = y + vec.y * ticks + ticks * rise;
        double bpz = z + vec.z * ticks;

        for (int i = 0; i < particles; i++) {
            double px = bpx + (Math.random() - 0.5) * posSpread;
            double py = bpy + (Math.random() - 0.5) * posSpread;
            double pz = bpz + (Math.random() - 0.5) * posSpread;
            ((ServerWorld)world).spawnParticle(ParticleTypes.PORTAL, px, py, pz, 1,0.2D, 0.12D, 0.1D, 0.06D);
        }

        if (ticks++ > maxLiveTime) {
            remove();
            ticks = 0;
        }
    }
}
