package svenhjol.charm.entity;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.Charm;
import svenhjol.charm.entity.goal.MoobloomPlantFlowerGoal;
import svenhjol.charm.module.Mooblooms;

import java.util.*;
import java.util.function.Consumer;

public class MoobloomEntity extends CowEntity implements Shearable {
    private static final String TYPE_TAG = "Type";
    private static final String POLLINATED_TAG = "Pollinated";

    private static final TrackedData<String> TYPE;
    private static final TrackedData<Boolean> POLLINATED;
    public static Map<Type, Identifier> TEXTURES = new HashMap<>();

    public MoobloomEntity(EntityType<? extends CowEntity> entityType, World world) {
        super(entityType, world);

        // set up the textures for each moobloom type
        TEXTURES = Util.make(Maps.newHashMap(), map -> {
            for (Type type : Type.values()) {
                map.put(type, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/" + type.name + ".png"));
            }
        });
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);

        List<Type> types = Arrays.asList(Type.values());
        Type type = types.get(random.nextInt(types.size()));
        setMoobloomType(type);

        return entityData;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, Type.ALLIUM.name());
        this.dataTracker.startTracking(POLLINATED, false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new MoobloomPlantFlowerGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack held = player.getStackInHand(hand);

        if (held.getItem() == Items.BOWL && !isBaby()) {
            if (!world.isClient && isPollinated()) {
                ItemStack stew;

                Optional<Pair<StatusEffect, Integer>> optionalFlower = getEffectFromFlower(this.getMoobloomType().flower);

                if (optionalFlower.isPresent()) {
                    Pair<StatusEffect, Integer> effectFromFlower = optionalFlower.get();

                    StatusEffect effect = effectFromFlower.getLeft();
                    int duration = effectFromFlower.getRight() * 2;

                    stew = new ItemStack(Items.SUSPICIOUS_STEW);
                    playSound(SoundEvents.ENTITY_MOOSHROOM_SUSPICIOUS_MILK, 1.0F, 1.0F);
                    SuspiciousStewItem.addEffectToStew(stew, effect, duration);
                } else {
                    stew = new ItemStack(Items.MUSHROOM_STEW);
                    playSound(SoundEvents.ENTITY_MOOSHROOM_MILK, 1.0F, 1.0F);
                }

                ItemStack out = ItemUsage.method_30270(held, player, stew, false);
                player.setStackInHand(hand, out);
                this.dataTracker.set(POLLINATED, false);
            }

            return ActionResult.success(world.isClient);

        } else if (held.getItem() == Items.SHEARS && isShearable()) {

            this.sheared(SoundCategory.PLAYERS);
            if (!world.isClient)
                held.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));

            return ActionResult.success(world.isClient);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putString(TYPE_TAG, this.getMoobloomType().name);
        tag.putBoolean(POLLINATED_TAG, this.dataTracker.get(POLLINATED));
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setMoobloomType(Type.fromName(tag.getString(TYPE_TAG)));

        if (tag.contains(POLLINATED_TAG))
            this.dataTracker.set(POLLINATED, tag.getBoolean(POLLINATED_TAG));
    }

    @Override
    public MoobloomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        MoobloomEntity entity = Mooblooms.MOOBLOOM.create(serverWorld);
        Type childType = serverWorld.random.nextFloat() < 0.5F ? this.getMoobloomType() : ((MoobloomEntity)passiveEntity).getMoobloomType();
        entity.setMoobloomType(childType);
        return entity;
    }

    public void pollinate() {
        world.playSound(null, getBlockPos(), SoundEvents.ENTITY_BEE_POLLINATE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        this.dataTracker.set(POLLINATED, true);
    }

    public boolean isPollinated() {
        return this.dataTracker.get(POLLINATED);
    }

    public Type getMoobloomType() {
        return Type.fromName(this.dataTracker.get(TYPE));
    }

    public void setMoobloomType(Type type) {
        this.dataTracker.set(TYPE, type.name);
    }

    public Identifier getMoobloomTexture() {
        return TEXTURES.getOrDefault(this.getMoobloomType(), TEXTURES.get(Type.ALLIUM));
    }

    public Optional<Pair<StatusEffect, Integer>> getEffectFromFlower(BlockState flower) {
        Block block = flower.getBlock();
        if (block instanceof FlowerBlock) {
            FlowerBlock flowerBlock = (FlowerBlock)block;
            return Optional.of(Pair.of(flowerBlock.getEffectInStew(), flowerBlock.getEffectInStewDuration()));
        }

        return Optional.empty();
    }

    public static boolean canSpawn(EntityType<MoobloomEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBaseLightLevel(pos, 0) > 8;
    }

    // copypasta from MooshroomEntity
    @Override
    public void sheared(SoundCategory shearedSoundCategory) {
        this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_MOOSHROOM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        if (!this.world.isClient) {
            ((ServerWorld)this.world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getBodyY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.remove();
            CowEntity cowEntity = EntityType.COW.create(this.world);
            cowEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
            cowEntity.setHealth(this.getHealth());
            cowEntity.bodyYaw = this.bodyYaw;
            if (this.hasCustomName()) {
                cowEntity.setCustomName(this.getCustomName());
                cowEntity.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistent()) {
                cowEntity.setPersistent();
            }

            cowEntity.setInvulnerable(this.isInvulnerable());
            this.world.spawnEntity(cowEntity);

            for(int i = 0; i < 5; ++i) {
                this.world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getBodyY(1.0D), this.getZ(), new ItemStack(this.getMoobloomType().flower.getBlock())));
            }
        }
    }

    @Override
    public boolean isShearable() {
        return isAlive() && !this.isBaby();
    }

    public enum Type {
        ALLIUM("allium", Blocks.ALLIUM.getDefaultState()),
        AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET.getDefaultState()),
        BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID.getDefaultState()),
        CORNFLOWER("cornflower", Blocks.CORNFLOWER.getDefaultState()),
        DANDELION("dandelion", Blocks.DANDELION.getDefaultState()),
        LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY.getDefaultState()),
        ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP.getDefaultState()),
        PINK_TULIP("pink_tulip", Blocks.PINK_TULIP.getDefaultState()),
        RED_TULIP("red_tulip", Blocks.RED_TULIP.getDefaultState()),
        WHITE_TULIP("white_tulip", Blocks.WHITE_TULIP.getDefaultState()),
        OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY.getDefaultState()),
        POPPY("poppy", Blocks.POPPY.getDefaultState());

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
        TYPE = DataTracker.registerData(MoobloomEntity.class, TrackedDataHandlerRegistry.STRING);
        POLLINATED = DataTracker.registerData(MoobloomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
