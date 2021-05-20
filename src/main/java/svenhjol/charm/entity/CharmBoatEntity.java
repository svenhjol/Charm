package svenhjol.charm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.BoatEntityAccessor;
import svenhjol.charm.module.AzaleaWood;
import svenhjol.charm.module.EbonyWood;
import svenhjol.charm.module.ExtraBoats;

import java.util.Locale;

public class CharmBoatEntity extends BoatEntity {
    private static final TrackedData<Integer> BOAT_TYPE;

    public CharmBoatEntity(EntityType<? extends CharmBoatEntity> entityType, World world) {
        super(entityType, world);
        ((BoatEntityAccessor)this).setPaddlePhases(new float[2]);
        this.inanimate = true;
    }

    public CharmBoatEntity(World world, double x, double y, double z) {
        this(ExtraBoats.CHARM_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(BOAT_TYPE, BoatType.WARPED.ordinal());
    }

    public BoatType getCharmBoatType() {
        return BoatType.byId(dataTracker.get(BOAT_TYPE));
    }

    public void setCharmBoatType(BoatType type) {
        dataTracker.set(BOAT_TYPE, type.ordinal());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Type", this.getCharmBoatType().name);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Type", 8))
            setCharmBoatType(BoatType.byName(nbt.getString("Type")));
    }

    @Override
    public Item asItem() {
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
                Charm.LOG.error(e.getMessage());
                boatType = CRIMSON;
            }

            return boatType;
        }

        public String asString() {
            return this.name.toLowerCase(Locale.ROOT);
        }
    }

    static {
        BOAT_TYPE = DataTracker.registerData(CharmBoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
