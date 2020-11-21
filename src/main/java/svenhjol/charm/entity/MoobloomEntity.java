package svenhjol.charm.entity;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
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

public class MoobloomEntity extends CowEntity {
    private static final String TYPE_TAG = "Type";
    private static final String POLLINATED_TAG = "Pollinated";
    private static final String EFFECT_ID_TAG = "EffectId";
    private static final String EFFECT_DURATION_TAG = "EffectDuration";
    private static final String EFFECT_AMPLIFIER_TAG = "EffectAmplifier";

    private static TrackedData<String> TYPE;
    public static Map<Type, Identifier> TEXTURES = new HashMap<>();

    private StatusEffect flowerEffect;
    private int flowerEffectDuration;
    private int flowerEffectAmplifier;
    private boolean pollinated;

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

        Optional<Pair<StatusEffect, Integer>> effectFromFlower = getEffectFromFlower(type.flower);
        effectFromFlower.ifPresent(e -> {
            MoobloomEntity.this.flowerEffect = e.getLeft();
            MoobloomEntity.this.flowerEffectDuration = e.getRight();
        });

        return entityData;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, Type.ALLIUM.name());
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new MoobloomPlantFlowerGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack held = player.getStackInHand(hand);

        if (held.getItem() == Items.BOWL && !this.isBaby()) {
            // TODO: handle timer
            if (this.flowerEffect != null) {
                ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
                SuspiciousStewItem.addEffectToStew(stew, this.flowerEffect, this.flowerEffectDuration);
            }

            return ActionResult.success(this.world.isClient);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putString(TYPE_TAG, this.getMoobloomType().name);
        if (this.flowerEffect != null) {
            tag.putByte(EFFECT_ID_TAG, (byte)StatusEffect.getRawId(this.flowerEffect));
            tag.putInt(EFFECT_DURATION_TAG, this.flowerEffectDuration);
            tag.putInt(EFFECT_AMPLIFIER_TAG, this.flowerEffectAmplifier);
            tag.putBoolean(POLLINATED_TAG, this.pollinated);
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setMoobloomType(Type.fromName(tag.getString(TYPE_TAG)));
        if (tag.contains(EFFECT_ID_TAG, 1))
            this.flowerEffect = StatusEffect.byRawId(tag.getByte(EFFECT_ID_TAG));

        if (tag.contains(EFFECT_DURATION_TAG, 3))
            this.flowerEffectDuration = tag.getInt(EFFECT_DURATION_TAG);

        if (tag.contains(EFFECT_AMPLIFIER_TAG))
            this.flowerEffectAmplifier = tag.getInt(EFFECT_AMPLIFIER_TAG);

        if (tag.contains(POLLINATED_TAG))
            this.pollinated = tag.getBoolean(POLLINATED_TAG);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public MoobloomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        MoobloomEntity entity = Mooblooms.MOOBLOOM.create(serverWorld);
        Type childType = serverWorld.random.nextFloat() < 0.5F ? this.getMoobloomType() : ((MoobloomEntity)passiveEntity).getMoobloomType();
        entity.setMoobloomType(childType);
        return entity;
    }

    @Environment(EnvType.CLIENT)
    protected void produceParticles(ParticleEffect particle) {
        for(int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(particle, this.getParticleX(1.0D), this.getRandomBodyY() + 1.0D, this.getParticleZ(1.0D), d, e, f);
        }
    }

    public void pollinate() {
        if (world.isClient) {
            produceParticles(ParticleTypes.LANDING_HONEY);
        } else {
            world.playSound(null, getBlockPos(), SoundEvents.ENTITY_BEE_POLLINATE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }

        this.pollinated = true;
    }

    public boolean isPollinated() {
        return this.pollinated;
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
    }
}
