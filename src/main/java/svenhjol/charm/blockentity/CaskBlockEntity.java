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
    public static final String PORTIONS_NBT = "Portions";
    public static final String EFFECTS_NBT = "Effects";
    public static final String DURATIONS_NBT = "Duration";
    public static final String AMPLIFIERS_NBT = "Amplifier";

    public int portions = 0;
    public Map<Identifier, Integer> durations = new HashMap<>();
    public Map<Identifier, Integer> amplifiers = new HashMap<>();
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

        NbtList list = nbt.getList(EFFECTS_NBT, 8);
        list.stream()
            .map(NbtElement::asString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.effects.add(new Identifier(item)));

        NbtCompound durations = nbt.getCompound(DURATIONS_NBT);
        NbtCompound amplifiers = nbt.getCompound(AMPLIFIERS_NBT);
        this.effects.forEach(effect -> {
            this.durations.put(effect, durations.getInt(effect.toString()));
            this.amplifiers.put(effect, amplifiers.getInt(effect.toString()));
        });
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(PORTIONS_NBT, this.portions);

        NbtCompound durations = new NbtCompound();
        NbtCompound amplifiers = new NbtCompound();

        NbtList effects = new NbtList();
        this.effects.forEach(effect -> {
            effects.add(NbtString.of(effect.toString()));
            durations.putInt(effect.toString(), this.durations.get(effect));
            amplifiers.putInt(effect.toString(), this.amplifiers.get(effect));
        });

        nbt.put(EFFECTS_NBT, effects);
        nbt.put(DURATIONS_NBT, durations);
        nbt.put(AMPLIFIERS_NBT, amplifiers);

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

        if (portions < 64) {

            // reset effects if fresh cask
            if (portions == 0)
                this.effects = new ArrayList<>();

            // water just dilutes the potion. process potion effects if not water
            if (potion != Potions.WATER) {

                List<StatusEffectInstance> effects = potion.getEffects();
                if (effects.isEmpty())
                    return false;

                effects.forEach(effect -> {
                    boolean higherAmplifier = false;

                    int duration = effect.getDuration();
                    int amplifier = effect.getAmplifier();

                    StatusEffect type = effect.getEffectType();
                    Identifier effectId = Registry.STATUS_EFFECT.getId(type);
                    if (effectId == null)
                        return;

                    if (!this.effects.contains(effectId))
                        this.effects.add(effectId);

                    if (this.amplifiers.containsKey(effectId)) {
                        int existingAmplifier = this.amplifiers.get(effectId);
                        higherAmplifier = amplifier > existingAmplifier;
                        this.amplifiers.put(effectId, Math.max(amplifier, existingAmplifier));
                    } else {
                        this.amplifiers.put(effectId, amplifier);
                    }

                    if (this.durations.containsKey(effectId)) {
                        int existingDuration = this.durations.get(effectId);
                        if (higherAmplifier) {
                            this.durations.put(effectId, duration);
                        } else {
                            this.durations.put(effectId, Math.max(existingDuration, duration / 4));
                        }
                    } else {
                        this.durations.put(effectId, duration);
                    }
                });
            }

            // weaken all durations
            this.effects.forEach(effectId -> {
                int existingDuration = this.durations.get(effectId);
                int scaled = Math.round(((64 - portions) / 64.0F) * (existingDuration - (float)(existingDuration / 6)));
                this.durations.put(effectId, Math.max(200, scaled));
            });

            this.portions++;

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

            for (Identifier effectId : this.effects) {
                Registry.STATUS_EFFECT.getOrEmpty(effectId).ifPresent(statusEffect
                    -> effects.add(new StatusEffectInstance(statusEffect, this.durations.get(effectId), this.amplifiers.get(effectId))));
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
