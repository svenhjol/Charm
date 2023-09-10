package svenhjol.charm.feature.mooblooms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class MoobloomEntity extends Cow implements Shearable {
    private static final String TYPE_TAG = "Type";
    private static final String POLLINATED_TAG = "Pollinated";
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> POLLINATED = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.BOOLEAN);
    public MoobloomEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.finalizeSpawn(level, difficulty, spawnReason, entityData, entityTag);

        var types = MoobloomType.getTypesForPos(level, blockPosition());
        var type = types.get(random.nextInt(types.size()));

        setMoobloomType(type);
        return entityData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TYPE, MoobloomType.ALLIUM.name());
        entityData.define(POLLINATED, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new MoobloomPlantFlowerGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        var level = level();

        if (held.getItem() == Items.BOWL && !isBaby()) {
            if (!level.isClientSide() && isPollinated()) {
                ItemStack stew;
                var optional = getMoobloomType().getFlower().getEffect();

                if (optional.isPresent()) {
                    var effectFromFlower = optional.get();
                    var effect = effectFromFlower.getLeft();
                    var duration = effectFromFlower.getRight() * 2;

                    stew = new ItemStack(Items.SUSPICIOUS_STEW);
                    SuspiciousStewItem.saveMobEffect(stew, effect, duration);
                } else {
                    stew = new ItemStack(Items.MUSHROOM_STEW);
                }

                playSound(Mooblooms.milkingSound.get(), 1.0F, 1.0F);

                var out = ItemUtils.createFilledResult(held, player, stew, false);
                player.setItemInHand(hand, out);
                entityData.set(POLLINATED, false);

                // TODO: Advancement
            }

            return InteractionResult.sidedSuccess(level.isClientSide());

        } else if (held.getItem() == Items.SHEARS && readyForShearing()) {

            shear(SoundSource.PLAYERS);
            if (!level.isClientSide()) {
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
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
    public MoobloomEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
        var entity = Mooblooms.entity.get().create(level);
        var childType = level.random.nextFloat() < 0.5F ?
            getMoobloomType() : ((MoobloomEntity)mob).getMoobloomType();

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

    public static boolean canSpawn(EntityType<MoobloomEntity> type, LevelAccessor level, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return level.getRawBrightness(pos, 0) > 8;
    }

    public void plantFlower() {
        var level = level();
        var pos = blockPosition();
        var flower = getMoobloomType().getFlower();

        if (flower.equals(FlowerBlockState.PINK_PETALS)) {
            level.setBlock(pos, flower.getBlockState().setValue(PinkPetalsBlock.AMOUNT,
                level.getRandom().nextInt(3) + 1), 2);
        } else if (flower.equals(FlowerBlockState.SUNFLOWER)) {
            ((BlockItem)Items.SUNFLOWER)
                .place(new DirectionalPlaceContext(level, pos, Direction.NORTH, ItemStack.EMPTY, Direction.NORTH));
        } else {
            level.setBlock(pos, flower.getBlockState(), 2);
        }

        level.levelEvent(2001, pos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
    }

    /**
     * Copypasta from Mooshroom entity.
     */
    @Override
    public void shear(SoundSource shearedSoundCategory) {
        var level = level();
        level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            ((ServerLevel)level).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            discard();

            var cow = EntityType.COW.create(level);
            if (cow == null) return;

            cow.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
            cow.setHealth(getHealth());
            cow.yBodyRot = yBodyRot;

            if (hasCustomName()) {
                cow.setCustomName(getCustomName());
                cow.setCustomNameVisible(isCustomNameVisible());
            }

            if (isPersistenceRequired()) {
                cow.setPersistenceRequired();
            }

            cow.setInvulnerable(isInvulnerable());
            level.addFreshEntity(cow);

            var flower = new ItemStack(getMoobloomType().getFlower().getBlock());
            for (int i = 0; i < 5; ++i) {
                level.addFreshEntity(new ItemEntity(level, getX(), getY(1.0D), getZ(), flower));
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby();
    }
}
