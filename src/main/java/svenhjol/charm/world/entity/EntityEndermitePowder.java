package svenhjol.charm.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityEndermitePowder extends Entity
{
    public int ticks = 0;
    private static final DataParameter<Integer> TARGET_X = EntityDataManager.createKey(EntityEndermitePowder.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TARGET_Z = EntityDataManager.createKey(EntityEndermitePowder.class, DataSerializers.VARINT);
    private static final String TAG_TARGET_X = "targetX";
    private static final String TAG_TARGET_Z = "targetZ";

    public EntityEndermitePowder(World world)
    {
        super(world);
        setSize(0, 0);
    }

    public EntityEndermitePowder(World world, int x, int z)
    {
        this(world);
        dataManager.set(TARGET_X, x);
        dataManager.set(TARGET_Z, z);
    }

    @Override
    protected void entityInit()
    {
        dataManager.register(TARGET_X, 0);
        dataManager.register(TARGET_Z, 0);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        double posSpread = 0.5;
        double scale = 0.2;
        double rise = 0.08;
        int maxLiveTime = 1000;
        int particles = 18;

        if((maxLiveTime - ticks) < particles)
            particles = (maxLiveTime - ticks);

        Vec3d vec = new Vec3d((double) dataManager.get(TARGET_X), posY, (double) dataManager.get(TARGET_Z)).subtract(posX, posY, posZ).normalize().scale(scale);
        double bpx = posX + vec.x * ticks;
        double bpy = posY + vec.y * ticks + ticks * rise;
        double bpz = posZ + vec.z * ticks;

        for(int i = 0; i < particles; i++) {
            double px = bpx + (Math.random() - 0.5) * posSpread;
            double py = bpy + (Math.random() - 0.5) * posSpread;
            double pz = bpz + (Math.random() - 0.5) * posSpread;
            world.spawnParticle(EnumParticleTypes.PORTAL, px, py, pz, 0.2, 0.12, 0.1);
        }

        ticks++;
        if(ticks > maxLiveTime || world.getBlockState(new BlockPos(bpx, bpy, bpz)).isBlockNormalCube())
            setDead();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        dataManager.set(TARGET_X, tag.getInteger(TAG_TARGET_X));
        dataManager.set(TARGET_Z, tag.getInteger(TAG_TARGET_Z));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger(TAG_TARGET_X, dataManager.get(TARGET_X));
        tag.setInteger(TAG_TARGET_Z, dataManager.get(TARGET_Z));
    }
}
