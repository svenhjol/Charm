package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.charmony.Resolve;

public class Entity extends net.minecraft.world.entity.Entity {
    private static final EndermitePowder ENDERMITE_POWDER = Resolve.feature(EndermitePowder.class);
    private static final int MAX_TICKS = 1000;
    private static final EntityDataAccessor<Integer> TARGET_X = SynchedEntityData.defineId(
        Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_Z = SynchedEntityData.defineId(
        Entity.class, EntityDataSerializers.INT);

    private static final String TARGET_X_TAG = "target_x";
    private static final String TARGET_Z_TAG = "target_z";

    int ticks = 0;

    public Entity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    public Entity(Level level, int x, int z) {
        this(ENDERMITE_POWDER.registers.entity.get(), level);
        entityData.set(TARGET_X, x);
        entityData.set(TARGET_Z, z);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TARGET_X, 0);
        entityData.define(TARGET_Z, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(TARGET_X, tag.getInt(TARGET_X_TAG));
        entityData.set(TARGET_Z, tag.getInt(TARGET_Z_TAG));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt(TARGET_X_TAG, entityData.get(TARGET_X));
        tag.putInt(TARGET_Z_TAG, entityData.get(TARGET_Z));
    }

    @Override
    public void tick() {
        super.tick();

        var level = level();
        var posSpread = 0.5d;
        var scale = 0.2d;
        var rise = 0.03d;
        var particles = 18;
        var x = blockPosition().getX();
        var y = blockPosition().getY();
        var z = blockPosition().getZ();

        var vec = new Vec3(entityData.get(TARGET_X), y, entityData.get(TARGET_Z))
            .subtract(x, y, z).normalize().scale(scale);

        var bpx = x + vec.x * ticks;
        var bpy = y + vec.y * ticks + ticks * rise;
        var bpz = z + vec.z * ticks;

        if (!level.isClientSide()) {
            var random = RandomSource.create();
            for (var i = 0; i < particles; i++) {
                var px = bpx + (random.nextDouble() - 0.5d) * posSpread;
                var py = bpy + (random.nextDouble() - 0.5d) * posSpread;
                var pz = bpz + (random.nextDouble() - 0.5d) * posSpread;
                ((ServerLevel)level).sendParticles(ParticleTypes.PORTAL, px, py, pz, 1, 0.2d, 0.12d, 0.1d, 0.06d);
            }
        }

        if (ticks++ > MAX_TICKS) {
            discard();
            ticks = 0;
        }
    }
}
