package svenhjol.charm.module.mooblooms;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import svenhjol.charm.Charm;

import javax.annotation.Nullable;
import java.util.*;

public class MoobloomEntity extends Cow implements Shearable {
    private static final String TYPE_NBT = "Type";
    private static final String POLLINATED_NBT = "Pollinated";

    private static final EntityDataAccessor<String> TYPE;
    private static final EntityDataAccessor<Boolean> POLLINATED;
    public static Map<Type, ResourceLocation> TEXTURES = new HashMap<>();

    public MoobloomEntity(EntityType<? extends Cow> entityType, Level world) {
        super(entityType, world);

        // set up the textures for each moobloom type
        TEXTURES = Util.make(Maps.newHashMap(), map -> {
            for (Type type : Type.values()) {
                map.put(type, new ResourceLocation(Charm.MOD_ID, "textures/entity/moobloom/" + type.name + ".png"));
            }
        });
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);

        List<Type> types = Arrays.asList(Type.values());
        Type type = types.get(random.nextInt(types.size()));
        setMoobloomType(type);

        return entityData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, Type.ALLIUM.name());
        this.entityData.define(POLLINATED, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MoobloomPlantFlowerGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        if (held.getItem() == Items.BOWL && !isBaby()) {
            if (!level.isClientSide && isPollinated()) {
                ItemStack stew;

                Optional<Pair<MobEffect, Integer>> optionalFlower = getEffectFromFlower(this.getMoobloomType().flower);

                if (optionalFlower.isPresent()) {
                    Pair<MobEffect, Integer> effectFromFlower = optionalFlower.get();

                    MobEffect effect = effectFromFlower.getLeft();
                    int duration = effectFromFlower.getRight() * 2;

                    stew = new ItemStack(Items.SUSPICIOUS_STEW);
                    playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0F, 1.0F);
                    SuspiciousStewItem.saveMobEffect(stew, effect, duration);
                } else {
                    stew = new ItemStack(Items.MUSHROOM_STEW);
                    playSound(SoundEvents.MOOSHROOM_MILK, 1.0F, 1.0F);
                }

                ItemStack out = ItemUtils.createFilledResult(held, player, stew, false);
                player.setItemInHand(hand, out);
                this.entityData.set(POLLINATED, false);

                Mooblooms.triggerMilkedMoobloom((ServerPlayer) player);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);

        } else if (held.getItem() == Items.SHEARS && readyForShearing()) {

            this.shear(SoundSource.PLAYERS);
            if (!level.isClientSide)
                held.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString(TYPE_NBT, this.getMoobloomType().name);
        nbt.putBoolean(POLLINATED_NBT, this.entityData.get(POLLINATED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setMoobloomType(Type.fromName(nbt.getString(TYPE_NBT)));

        if (nbt.contains(POLLINATED_NBT))
            this.entityData.set(POLLINATED, nbt.getBoolean(POLLINATED_NBT));
    }

    @Override
    public MoobloomEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        MoobloomEntity entity = Mooblooms.MOOBLOOM.create(serverWorld);
        Type childType = serverWorld.random.nextFloat() < 0.5F ? this.getMoobloomType() : ((MoobloomEntity)passiveEntity).getMoobloomType();
        entity.setMoobloomType(childType);
        return entity;
    }

    public void pollinate() {
        level.playSound(null, blockPosition(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        this.entityData.set(POLLINATED, true);
    }

    public boolean isPollinated() {
        return this.entityData.get(POLLINATED);
    }

    public Type getMoobloomType() {
        return Type.fromName(this.entityData.get(TYPE));
    }

    public void setMoobloomType(Type type) {
        this.entityData.set(TYPE, type.name);
    }

    public ResourceLocation getMoobloomTexture() {
        return TEXTURES.getOrDefault(this.getMoobloomType(), TEXTURES.get(Type.ALLIUM));
    }

    public Optional<Pair<MobEffect, Integer>> getEffectFromFlower(BlockState flower) {
        Block block = flower.getBlock();
        if (block instanceof FlowerBlock) {
            FlowerBlock flowerBlock = (FlowerBlock)block;
            return Optional.of(Pair.of(flowerBlock.getSuspiciousStewEffect(), flowerBlock.getEffectDuration()));
        }

        return Optional.empty();
    }

    public static boolean canSpawn(EntityType<MoobloomEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return world.getRawBrightness(pos, 0) > 8;
    }

    // copypasta from MooshroomEntity
    @Override
    public void shear(SoundSource shearedSoundCategory) {
        this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        if (!this.level.isClientSide) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.discard();
            Cow cowEntity = EntityType.COW.create(this.level);
            cowEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            cowEntity.setHealth(this.getHealth());
            cowEntity.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
                cowEntity.setCustomName(this.getCustomName());
                cowEntity.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistenceRequired()) {
                cowEntity.setPersistenceRequired();
            }

            cowEntity.setInvulnerable(this.isInvulnerable());
            this.level.addFreshEntity(cowEntity);

            for(int i = 0; i < 5; ++i) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), new ItemStack(this.getMoobloomType().flower.getBlock())));
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !this.isBaby();
    }

    public enum Type {
        ALLIUM("allium", Blocks.ALLIUM.defaultBlockState()),
        AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET.defaultBlockState()),
        BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID.defaultBlockState()),
        CORNFLOWER("cornflower", Blocks.CORNFLOWER.defaultBlockState()),
        DANDELION("dandelion", Blocks.DANDELION.defaultBlockState()),
        LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY.defaultBlockState()),
        ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP.defaultBlockState()),
        PINK_TULIP("pink_tulip", Blocks.PINK_TULIP.defaultBlockState()),
        RED_TULIP("red_tulip", Blocks.RED_TULIP.defaultBlockState()),
        WHITE_TULIP("white_tulip", Blocks.WHITE_TULIP.defaultBlockState()),
        OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY.defaultBlockState()),
        POPPY("poppy", Blocks.POPPY.defaultBlockState());

        private final String name;
        private final BlockState flower;

        Type(String name, BlockState flower) {
            this.name = name;
            this.flower = flower;
        }

        public BlockState getFlower() {
            return this.flower;
        }

        private static Type fromName(String name) {
            Type[] values = values();
            for (Type value : values) {
                if (value.name.equals(name))
                    return value;
            }

            return ALLIUM;
        }
    }

    static {
        TYPE = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.STRING);
        POLLINATED = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
