package svenhjol.charm.mixin.variant_chests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.variant_chest_boats.IVariantChestBoat;

/**
 * Adds an extra VARIANT_CHEST property to all ChestBoat entities.
 */
@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(value = ChestBoat.class)
public abstract class ChestBoatMixin extends Boat implements IVariantChestBoat, ContainerEntity {
    @Unique
    private static final String VARIANT_CHEST_TAG = "variant_chest";
    @Unique
    private static final EntityDataAccessor<String> DATA_ID_VARIANT_CHEST = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.STRING);

    public ChestBoatMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_VARIANT_CHEST, "");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readChestVehicleSaveData(compoundTag);
        setVariantChest(compoundTag.getString(VARIANT_CHEST_TAG));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addChestVehicleSaveData(compoundTag);
        compoundTag.putString(VARIANT_CHEST_TAG, getVariantChest());
    }

    public void setVariantChest(String type) {
        this.entityData.set(DATA_ID_VARIANT_CHEST, type);
    }

    public String getVariantChest() {
        return this.entityData.get(DATA_ID_VARIANT_CHEST);
    }
}
