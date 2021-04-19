package svenhjol.charm.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import svenhjol.charm.base.helper.PotionHelper;
import svenhjol.charm.module.Casks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaskBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public static final int MAX_PORTIONS = 64;

    public static final String PORTIONS_NBT = "Portions";
    public static final String EFFECTS_NBT = "Effects";
    public static final String DURATIONS_NBT = "Duration";
    public static final String AMPLIFIERS_NBT = "Amplifier";
    public static final String DILUTIONS_NBT = "Dilutions";

    public int portions = 0;
    public Map<Identifier, Integer> durations = new HashMap<>();
    public Map<Identifier, Integer> amplifiers = new HashMap<>();
    public Map<Identifier, Integer> dilutions = new HashMap<>();
    public List<Identifier> effects = new ArrayList<>();

    public CaskBlockEntity(BlockPos pos, BlockState state) {
        super(Casks.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.portions = nbt.getInt(PORTIONS_NBT);
        this.effects = new ArrayList<>();
        this.durations = new HashMap<>();
        this.amplifiers = new HashMap<>();
        this.dilutions = new HashMap<>();

        NbtList list = nbt.getList(EFFECTS_NBT, 8);
        list.stream()
            .map(NbtElement::asString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.effects.add(new Identifier(item)));

        NbtCompound durations = nbt.getCompound(DURATIONS_NBT);
        NbtCompound amplifiers = nbt.getCompound(AMPLIFIERS_NBT);
        NbtCompound dilutions = nbt.getCompound(DILUTIONS_NBT);
        this.effects.forEach(effect -> {
            this.durations.put(effect, durations.getInt(effect.toString()));
            this.amplifiers.put(effect, amplifiers.getInt(effect.toString()));
            this.dilutions.put(effect, dilutions.getInt(effect.toString()));
        });
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(PORTIONS_NBT, this.portions);

        NbtCompound durations = new NbtCompound();
        NbtCompound amplifiers = new NbtCompound();
        NbtCompound dilutions = new NbtCompound();

        NbtList effects = new NbtList();
        this.effects.forEach(effect -> {
            effects.add(NbtString.of(effect.toString()));
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
    public void fromClientTag(NbtCompound nbt) {
        readNbt(nbt);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound nbtCompound) {
        return writeNbt(nbtCompound);
    }

    public boolean add(World world, BlockPos pos, BlockState state, ItemStack input) {
        if (input.getItem() != Items.POTION)
            return false;

        Potion potion = PotionUtil.getPotion(input);
        if (potion == Potions.EMPTY)
            return false;

        if (portions < MAX_PORTIONS) {

            // reset effects if fresh cask
            if (portions == 0)
                this.effects = new ArrayList<>();

            // water just dilutes the potion. process potion effects if not water
            if (potion != Potions.WATER) {

                List<StatusEffectInstance> effects = potion.getEffects();
                if (effects.isEmpty())
                    return false;

                effects.forEach(effect -> {
                    boolean changedAmplifier = false;

                    int duration = effect.getDuration();
                    int amplifier = effect.getAmplifier();

                    StatusEffect type = effect.getEffectType();
                    Identifier effectId = Registry.STATUS_EFFECT.getId(type);
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

            markDirty();
            sync();

            input.decrement(1);
            return true;
        }

        return false;
    }

    @Nullable
    public ItemStack take(World world, BlockPos pos, BlockState state, ItemStack container) {
        // might support other containers in future
        if (container.getItem() != Items.GLASS_BOTTLE)
            return null;

        if (this.portions > 0) {
            // create a potion from the cask's contents
            ItemStack bottle = PotionHelper.getFilledWaterBottle();
            List<StatusEffectInstance> effects = new ArrayList<>();

            if (this.effects.isEmpty())
                return null;

            for (Identifier effectId : this.effects) {
                Registry.STATUS_EFFECT.getOrEmpty(effectId).ifPresent(statusEffect -> {
                    int duration = this.durations.get(effectId);
                    int amplifier = this.amplifiers.get(effectId);
                    int dilution = this.dilutions.get(effectId);

                    effects.add(new StatusEffectInstance(statusEffect, duration / dilution, amplifier));
                });
            }

            PotionUtil.setCustomPotionEffects(bottle, effects);
            container.decrement(1);

            // if no more portions in the cask, flush out the cask data
            if (--portions <= 0)
                this.flush(world, pos, state);

            bottle.setCustomName(new TranslatableText("item.charm.home_brew"));
            return bottle;
        }

        return null;
    }

    private void flush(World world, BlockPos pos, BlockState state) {
        this.effects = new ArrayList<>();
        this.durations = new HashMap<>();
        this.dilutions = new HashMap<>();
        this.amplifiers = new HashMap<>();
        this.portions = 0;

        markDirty();
        sync();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isClient) {
            for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld)world, pos, 5)) {
                player.networkHandler.sendPacket(this.toUpdatePacket());
            }
            BlockState state = world.getBlockState(pos);
            world.updateListeners(pos, state, state, 1);
        }
    }
}
