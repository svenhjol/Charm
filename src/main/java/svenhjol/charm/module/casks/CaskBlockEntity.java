package svenhjol.charm.module.casks;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.PotionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaskBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public static final String PORTIONS_NBT = "Portions";
    public static final String EFFECTS_NBT = "Effects";
    public static final String DURATIONS_NBT = "Duration";
    public static final String AMPLIFIERS_NBT = "Amplifier";
    public static final String DILUTIONS_NBT = "Dilutions";
    public static final String NAME_NBT = "Name";
    public static final String FERMENTATION_NBT = "Fermentation";

    public int portions = 0;
    public int fermentation = 0;
    public String name = "";
    public Map<ResourceLocation, Integer> durations = new HashMap<>();
    public Map<ResourceLocation, Integer> amplifiers = new HashMap<>();
    public Map<ResourceLocation, Integer> dilutions = new HashMap<>();
    public List<ResourceLocation> effects = new ArrayList<>();

    public CaskBlockEntity(BlockPos pos, BlockState state) {
        super(Casks.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.name = nbt.getString(NAME_NBT);
        this.portions = nbt.getInt(PORTIONS_NBT);
        this.fermentation = nbt.getInt(FERMENTATION_NBT);
        this.effects = new ArrayList<>();
        this.durations = new HashMap<>();
        this.amplifiers = new HashMap<>();
        this.dilutions = new HashMap<>();

        ListTag list = nbt.getList(EFFECTS_NBT, 8);
        list.stream()
            .map(Tag::getAsString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.effects.add(new ResourceLocation(item)));

        CompoundTag durations = nbt.getCompound(DURATIONS_NBT);
        CompoundTag amplifiers = nbt.getCompound(AMPLIFIERS_NBT);
        CompoundTag dilutions = nbt.getCompound(DILUTIONS_NBT);
        this.effects.forEach(effect -> {
            this.durations.put(effect, durations.getInt(effect.toString()));
            this.amplifiers.put(effect, amplifiers.getInt(effect.toString()));
            this.dilutions.put(effect, dilutions.getInt(effect.toString()));
        });
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);

        nbt.putString(NAME_NBT, this.name);
        nbt.putInt(PORTIONS_NBT, this.portions);
        nbt.putInt(FERMENTATION_NBT, this.fermentation);

        CompoundTag durations = new CompoundTag();
        CompoundTag amplifiers = new CompoundTag();
        CompoundTag dilutions = new CompoundTag();

        ListTag effects = new ListTag();
        this.effects.forEach(effect -> {
            effects.add(StringTag.valueOf(effect.toString()));
            durations.putInt(effect.toString(), this.durations.get(effect));
            amplifiers.putInt(effect.toString(), this.amplifiers.get(effect));
            dilutions.putInt(effect.toString(), this.dilutions.get(effect));
        });

        nbt.put(EFFECTS_NBT, effects);
        nbt.put(DURATIONS_NBT, durations);
        nbt.put(AMPLIFIERS_NBT, amplifiers);
        nbt.put(DILUTIONS_NBT, dilutions);

        return nbt;
    }

    @Override
    public void fromClientTag(CompoundTag nbt) {
        load(nbt);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag nbtCompound) {
        return save(nbtCompound);
    }

    public boolean add(Level world, BlockPos pos, BlockState state, ItemStack input) {
        if (input.getItem() != Items.POTION)
            return false;

        Potion potion = PotionUtils.getPotion(input);
        List<MobEffectInstance> customEffects = PotionUtils.getCustomEffects(input);
        if (potion == Potions.EMPTY)
            return false;

        if (portions < Casks.maxPortions) {

            // reset effects if fresh cask
            if (portions == 0)
                this.effects = new ArrayList<>();

            // potions without effects just dilute the mix
            if (potion != Potions.WATER || !customEffects.isEmpty()) {

                List<MobEffectInstance> effects = customEffects.isEmpty() && !potion.getEffects().isEmpty() ? potion.getEffects() : customEffects;

                // strip out immediate effects and other weird things
                effects = effects.stream()
                    .filter(e -> e.getDuration() > 1)
                    .collect(Collectors.toList());

                if (effects.isEmpty())
                    return false;

                effects.forEach(effect -> {
                    boolean changedAmplifier = false;

                    int duration = effect.getDuration();
                    int amplifier = effect.getAmplifier();

                    MobEffect type = effect.getEffect();
                    ResourceLocation effectId = Registry.MOB_EFFECT.getKey(type);
                    if (effectId == null)
                        return;

                    if (!this.effects.contains(effectId)) {
                        this.effects.add(effectId);
                    }

                    if (this.amplifiers.containsKey(effectId)) {
                        int existingAmplifier = this.amplifiers.get(effectId);
                        changedAmplifier = amplifier != existingAmplifier;
                    }
                    this.amplifiers.put(effectId, amplifier);

                    if (!this.durations.containsKey(effectId)) {
                        this.durations.put(effectId, duration);
                    } else {
                        int existingDuration = this.durations.get(effectId);
                        if (changedAmplifier) {
                            this.durations.put(effectId, duration);
                        } else {
                            this.durations.put(effectId, existingDuration + duration);
                        }
                    }
                });
            }

            this.portions++;

            this.effects.forEach(effectId -> {
                if (!this.dilutions.containsKey(effectId)) {
                    this.dilutions.put(effectId, portions);
                } else {
                    int existingDilution = this.dilutions.get(effectId);
                    this.dilutions.put(effectId, existingDilution + 1);
                }
            });

            setChanged();
            sync();

            input.shrink(1);
            return true;
        }

        return false;
    }

    @Nullable
    public ItemStack take(Level world, BlockPos pos, BlockState state, ItemStack container) {
        // might support other containers in future
        if (container.getItem() != Items.GLASS_BOTTLE)
            return null;

        if (this.portions > 0) {
            // create a potion from the cask's contents
            ItemStack bottle = PotionHelper.getFilledWaterBottle();
            List<MobEffectInstance> effects = new ArrayList<>();

            for (ResourceLocation effectId : this.effects) {
                Registry.MOB_EFFECT.getOptional(effectId).ifPresent(statusEffect -> {
                    int duration = this.durations.get(effectId);
                    int amplifier = this.amplifiers.get(effectId);
                    int dilution = this.dilutions.get(effectId);

                    effects.add(new MobEffectInstance(statusEffect, (duration / dilution) * fermentation, amplifier));
                });
            }

            Component bottleName;

            if (!effects.isEmpty()) {
                PotionUtils.setCustomEffects(bottle, effects);
                if (!name.isEmpty()) {
                    bottleName = new TextComponent(name);
                } else {
                    bottleName = new TranslatableComponent("item.charm.home_brew");
                }
                bottle.setHoverName(bottleName);
            }

            container.shrink(1);

            // if no more portions in the cask, flush out the cask data
            if (--portions <= 0)
                this.flush(world, pos, state);

            return bottle;
        }

        return null;
    }

    public void ferment() {
        if (this.fermentation < 5) {
            this.fermentation++;
        }
    }

    private void flush(Level world, BlockPos pos, BlockState state) {
        this.effects = new ArrayList<>();
        this.durations = new HashMap<>();
        this.dilutions = new HashMap<>();
        this.amplifiers = new HashMap<>();
        this.portions = 0;
        this.fermentation = 0;

        setChanged();
        sync();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            for (ServerPlayer player : PlayerLookup.around((ServerLevel)level, worldPosition, 5)) {
                player.connection.send(this.getUpdatePacket());
            }
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 1);
        }
    }
}
