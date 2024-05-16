package svenhjol.charm.feature.mooblooms.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import svenhjol.charm.api.enums.EventResult;
import svenhjol.charm.feature.mooblooms.Mooblooms;
import svenhjol.charm.foundation.feature.FeatureResolver;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class Moobloom extends Cow implements Shearable, FeatureResolver<Mooblooms> {
    private static final String TYPE_TAG = "Type";
    private static final String POLLINATED_TAG = "Pollinated";

    public static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> POLLINATED = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);

    public Moobloom(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);

        var types = MoobloomType.getTypesForPos(level, blockPosition());
        var type = types.get(random.nextInt(types.size()));

        setMoobloomType(type);
        return spawnGroupData;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, MoobloomType.ALLIUM.name());
        builder.define(POLLINATED, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new PlantFlowerGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var result = feature().handlers.interact(this, player, hand);
        if (result != EventResult.PASS) {
            return result.asInteractionResult(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(TYPE_TAG, getMoobloomType().getName());
        tag.putBoolean(POLLINATED_TAG, entityData.get(POLLINATED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setMoobloomType(MoobloomType.fromName(tag.getString(TYPE_TAG)));

        if (tag.contains(POLLINATED_TAG)) {
            entityData.set(POLLINATED, tag.getBoolean(POLLINATED_TAG));
        }
    }

    @Override
    public Moobloom getBreedOffspring(ServerLevel level, AgeableMob mob) {
        var entity = feature().registers.entity.get().create(level);
        var childType = level.random.nextFloat() < 0.5F ?
            getMoobloomType() : ((Moobloom)mob).getMoobloomType();

        if (entity != null) {
            entity.setMoobloomType(childType);
            return entity;
        }

        return null;
    }

    public void pollinate() {
        var level = level();
        level.playSound(null, blockPosition(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        entityData.set(POLLINATED, true);
    }

    public boolean isPollinated() {
        return entityData.get(POLLINATED);
    }

    public MoobloomType getMoobloomType() {
        return MoobloomType.fromName(entityData.get(TYPE));
    }

    public void setMoobloomType(MoobloomType type) {
        entityData.set(TYPE, type.getName());
    }

    public ResourceLocation getMoobloomTexture() {
        return getMoobloomType().getTexture();
    }

    public static boolean canSpawn(EntityType<Moobloom> type, LevelAccessor level, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return level.getRawBrightness(pos, 0) > 8;
    }

    public void plantFlower() {
        feature().handlers.plantFlower(this);
    }

    @Override
    public void shear(SoundSource soundSource) {
        feature().handlers.shear(this, soundSource);
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby();
    }

    @Override
    public Class<Mooblooms> typeForFeature() {
        return Mooblooms.class;
    }
}
