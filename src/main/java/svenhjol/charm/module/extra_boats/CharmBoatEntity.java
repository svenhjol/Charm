package svenhjol.charm.module.extra_boats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class CharmBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> BOAT_TYPE;

    public CharmBoatEntity(EntityType<? extends CharmBoatEntity> entityType, Level level) {
        super(entityType, level);
        this.paddlePositions = new float[2];
        this.blocksBuilding = true;
    }

    public CharmBoatEntity(Level level, double x, double y, double z) {
        this(ExtraBoats.CHARM_BOAT, level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(BOAT_TYPE, 0);
    }

    public String getCharmBoatType() {
        return new ArrayList<>(ExtraBoats.BOATS.keySet()).get(entityData.get(BOAT_TYPE));
    }

    public void setCharmBoatType(String type) {
        entityData.set(BOAT_TYPE, new ArrayList<>(ExtraBoats.BOATS.keySet()).indexOf(type));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Type", getCharmBoatType());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Type", 8)) {
            setCharmBoatType(nbt.getString("Type"));
        }
    }

    @Override
    public Item getDropItem() {
        String charmBoatType = getCharmBoatType();
        if (!ExtraBoats.BOATS.containsKey(charmBoatType)) {
            return Items.OAK_BOAT; // we need to return some kind of default boat if it goes wrong
        }

        return ExtraBoats.BOATS.get(charmBoatType);
    }

    static {
        BOAT_TYPE = SynchedEntityData.defineId(CharmBoatEntity.class, EntityDataSerializers.INT);
    }
}
