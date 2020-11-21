package svenhjol.charm.entity;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.module.Mooblooms;

import java.util.Map;

public class MoobloomEntity extends CowEntity {
    private static final String TYPE_TAG = "Type";
    private static final String EFFECT_ID_TAG = "EffectId";
    private static final String EFFECT_DURATION_TAG = "EffectDuration";
    private static final String EFFECT_AMPLIFIER_TAG = "EffectAmplifier";

    private static TrackedData<String> TYPE;
    public static Map<Type, Identifier> TEXTURES;

    private StatusEffect flowerEffect;
    private int flowerEffectDuration;
    private int flowerEffectAmplifier;

    public MoobloomEntity(EntityType<? extends CowEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, Type.ALLIUM.name());
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
    }

    public Type getMoobloomType() {
        return Type.fromName(this.dataTracker.get(TYPE));
    }

    public void setMoobloomType(Type type) {
        this.dataTracker.set(TYPE, type.name);
    }

    @Override
    public MoobloomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        MoobloomEntity entity = Mooblooms.MOOBLOOM.create(serverWorld);
        Type childType = serverWorld.random.nextFloat() < 0.5F ? this.getMoobloomType() : ((MoobloomEntity)passiveEntity).getMoobloomType();
        entity.setMoobloomType(childType);
        return entity;
    }

    public enum Type {
        ALLIUM("allium", Blocks.ALLIUM.getDefaultState()),
        AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET.getDefaultState()),
        BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID.getDefaultState()),
        CORNFLOWER("cornflower", Blocks.CORNFLOWER.getDefaultState()),
        DANDELION("dandelion", Blocks.DANDELION.getDefaultState()),
        LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY.getDefaultState()),
        ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP.getDefaultState()),
        OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY.getDefaultState()),
        POPPY("poppy", Blocks.POPPY.getDefaultState()),
        WITHER_ROSE("wither_rose", Blocks.WITHER_ROSE.getDefaultState());

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
        TEXTURES = Util.make(Maps.newHashMap(), map -> {
            map.put(Type.ALLIUM, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/allium.png"));
            map.put(Type.AZURE_BLUET, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/azure_bluet.png"));
            map.put(Type.BLUE_ORCHID, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/blue_orchid.png"));
            map.put(Type.CORNFLOWER, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/cornflower.png"));
            map.put(Type.DANDELION, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/dandelion.png"));
            map.put(Type.LILY_OF_THE_VALLEY, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/lily_of_the_valley.png"));
            map.put(Type.ORANGE_TULIP, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/orange_tulip.png"));
            map.put(Type.OXEYE_DAISY, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/oxeye_daisy.png"));
            map.put(Type.POPPY, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/poppy.png"));
            map.put(Type.WITHER_ROSE, new Identifier(Charm.MOD_ID, "textures/entity/moobloom/wither_rose.png"));
        });
    }
}
