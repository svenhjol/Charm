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
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.mixin.accessor.BoatAccessor;
import svenhjol.charm.module.azalea_wood.AzaleaWood;
import svenhjol.charm.module.ebony_wood.EbonyWood;

import java.util.Locale;

public class CharmBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> BOAT_TYPE;

    public CharmBoatEntity(EntityType<? extends CharmBoatEntity> entityType, Level world) {
        super(entityType, world);
        ((BoatAccessor)this).setPaddlePositions(new float[2]);
        this.blocksBuilding = true;
    }

    public CharmBoatEntity(Level world, double x, double y, double z) {
        this(ExtraBoats.CHARM_BOAT, world);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(BOAT_TYPE, BoatType.WARPED.ordinal());
    }

    public BoatType getCharmBoatType() {
        return BoatType.byId(entityData.get(BOAT_TYPE));
    }

    public void setCharmBoatType(BoatType type) {
        entityData.set(BOAT_TYPE, type.ordinal());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Type", this.getCharmBoatType().name);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Type", 8))
            setCharmBoatType(BoatType.byName(nbt.getString("Type")));
    }

    @Override
    public Item getDropItem() {
        switch (getCharmBoatType()) {
            case AZALEA:
                return AzaleaWood.BOAT;
            case CRIMSON:
                return ExtraBoats.CRIMSON_BOAT;
            case EBONY:
                return EbonyWood.BOAT;
            case WARPED:
                return ExtraBoats.WARPED_BOAT;
            default:
                return Items.OAK_BOAT; // lol
        }
    }

    public enum BoatType {
        AZALEA("azalea"),
        CRIMSON("crimson"),
        EBONY("ebony"),
        WARPED("warped");

        private final String name;

        BoatType(String name) {
            this.name = name;
        }

        public static BoatType byId(int id) {
            BoatType[] values = values();
            if (id < values.length) {
                return values[id];
            } else {
                return values[0];
            }
        }

        public static BoatType byName(String name) {
            String upper = name.toUpperCase(Locale.ROOT);
            BoatType boatType;

            try {
                boatType = valueOf(upper);
            } catch (IllegalArgumentException e) {
                LogHelper.error(BoatType.class, e.getMessage());
                boatType = CRIMSON;
            }

            return boatType;
        }

        public String asString() {
            return this.name.toLowerCase(Locale.ROOT);
        }
    }

    static {
        BOAT_TYPE = SynchedEntityData.defineId(CharmBoatEntity.class, EntityDataSerializers.INT);
    }
}
