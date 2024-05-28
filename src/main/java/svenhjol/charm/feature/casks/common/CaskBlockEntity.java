package svenhjol.charm.feature.casks.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.block.entity.CharmBlockEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaskBlockEntity extends CharmBlockEntity<Casks> implements Container, WorldlyContainer, Nameable {
    private static final Casks CASKS = Resolve.feature(Casks.class);
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1};

    public static final String NAME_TAG = "name";
    public static final String BOTTLES_TAG = "bottles";
    public static final String EFFECTS_TAG = "effects";
    public static final String DURATIONS_TAG = "duration";
    public static final String AMPLIFIERS_TAG = "amplifier";
    public static final String DILUTIONS_TAG = "dilutions";

    // Mapped by data components.
    public Component name;
    public int bottles = 0;
    public Map<ResourceLocation, Integer> durations = new HashMap<>();
    public Map<ResourceLocation, Integer> amplifiers = new HashMap<>();
    public Map<ResourceLocation, Integer> dilutions = new HashMap<>();
    public List<ResourceLocation> effects = new ArrayList<>();

    // Queue for hopper items.
    public final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public CaskBlockEntity(BlockPos pos, BlockState state) {
        super(CASKS.registers.blockEntity.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains(NAME_TAG)) {
            this.name = Component.Serializer.fromJson(tag.getString(NAME_TAG), provider);
        }
        this.bottles = tag.getInt(BOTTLES_TAG);
        this.effects.clear();
        this.durations.clear();
        this.amplifiers.clear();
        this.dilutions.clear();

        ListTag list = tag.getList(EFFECTS_TAG, 8);
        list.stream()
            .map(Tag::getAsString)
            .map(i -> i.replace("\"", "")) // madness
            .forEach(item -> this.effects.add(ResourceLocation.parse(item)));

        CompoundTag durations = tag.getCompound(DURATIONS_TAG);
        CompoundTag amplifiers = tag.getCompound(AMPLIFIERS_TAG);
        CompoundTag dilutions = tag.getCompound(DILUTIONS_TAG);
        this.effects.forEach(effect -> {
            this.durations.put(effect, durations.getInt(effect.toString()));
            this.amplifiers.put(effect, amplifiers.getInt(effect.toString()));
            this.dilutions.put(effect, dilutions.getInt(effect.toString()));
        });

        ContainerHelper.loadAllItems(tag, items, provider);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        if (this.name != null) {
            tag.putString(NAME_TAG, Component.Serializer.toJson(this.name, provider));
        }

        tag.putInt(BOTTLES_TAG, this.bottles);

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

        tag.put(EFFECTS_TAG, effects);
        tag.put(DURATIONS_TAG, durations);
        tag.put(AMPLIFIERS_TAG, amplifiers);
        tag.put(DILUTIONS_TAG, dilutions);

        ContainerHelper.saveAllItems(tag, items, provider);
    }

    @SuppressWarnings("unused")
    public static void serverTick(Level level, BlockPos pos, BlockState state, CaskBlockEntity cask) {
        CASKS.handlers.hopperAddToCask(cask);
    }

    @SuppressWarnings("Java8MapApi")
    public boolean add(ItemStack input) {
        if (!feature().handlers.isValidPotion(input)) {
            return false;
        }

        var potion = feature().handlers.getPotion(input);
        List<MobEffectInstance> potionEffects = new ArrayList<>();
        potion.getAllEffects().forEach(potionEffects::add);

        if (!potion.hasEffects()) {
            return false;
        }

        if (bottles < feature().maxBottles()) {

            // Reset effects if fresh cask
            if (bottles == 0) {
                this.effects.clear();
            }

            // Potions without effects just dilute the mix
            if (potion.is(Potions.WATER) || !potionEffects.isEmpty()) {
                var currentEffects = potionEffects.isEmpty() && !potion.customEffects().isEmpty() ? potion.customEffects() : potionEffects;

                // Strip out immediate effects and other weird things
                currentEffects = currentEffects.stream()
                    .filter(e -> e.getDuration() > 1)
                    .toList();

                currentEffects.forEach(effect -> {
                    var changedAmplifier = false;
                    var duration = effect.getDuration();
                    var amplifier = effect.getAmplifier();
                    var type = effect.getEffect();
                    var effectId = BuiltInRegistries.MOB_EFFECT.getKey(type.value());

                    if (effectId == null) {
                        return;
                    }

                    if (!effects.contains(effectId)) {
                        effects.add(effectId);
                    }

                    if (amplifiers.containsKey(effectId)) {
                        int existingAmplifier = amplifiers.get(effectId);
                        changedAmplifier = amplifier != existingAmplifier;
                    }
                    amplifiers.put(effectId, amplifier);

                    if (!durations.containsKey(effectId)) {
                        durations.put(effectId, duration);
                    } else {
                        var existingDuration = durations.get(effectId);
                        if (changedAmplifier) {
                            durations.put(effectId, duration);
                        } else {
                            durations.put(effectId, existingDuration + duration);
                        }
                    }
                });
            }

            bottles++;

            effects.forEach(effectId -> {
                if (!dilutions.containsKey(effectId)) {
                    dilutions.put(effectId, bottles);
                } else {
                    int existingDilution = dilutions.get(effectId);
                    dilutions.put(effectId, existingDilution + 1);
                }
            });

            if (level != null) {
                level.playSound(null, getBlockPos(), feature().registers.addSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }

            setChanged();
            return true;
        }

        return false;
    }

    @Nullable
    public ItemStack take() {
        if (this.bottles > 0) {
            var bottle = getBottle();
            removeBottle();

            // Play sound
            if (level != null) {
                var pos = getBlockPos();
                if (bottles > 0) {
                    level.playSound(null, pos, feature().registers.takeSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    level.playSound(null, pos, feature().registers.emptySound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }

            return bottle;
        }

        return null;
    }

    private ItemStack getBottle() {
        // create a potion from the cask's contents
        List<MobEffectInstance> effects = new ArrayList<>();

        for (var effectId : this.effects) {
            var holder = BuiltInRegistries.MOB_EFFECT.getHolder(effectId);
            if (holder.isEmpty()) continue;

            int duration = this.durations.get(effectId);
            int amplifier = this.amplifiers.get(effectId);
            int dilution = this.dilutions.get(effectId);

            effects.add(new MobEffectInstance(holder.get(), duration / dilution, amplifier));
        }

        if (!effects.isEmpty()) {
            return feature().handlers.makeCustomPotion(effects);
        }

        return feature().handlers.getFilledWaterBottle();
    }

    private void removeBottle() {
        // if no more bottles in the cask, flush out the cask data
        if (--bottles <= 0) {
            this.flush();
        }

        setChanged();
    }

    private void flush() {
        this.effects.clear();
        this.durations.clear();
        this.dilutions.clear();
        this.amplifiers.clear();
        this.bottles = 0;

        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        var caskData = input.getOrDefault(feature().registers.data.get(), CaskContentsData.EMPTY);

        this.name = input.get(DataComponents.CUSTOM_NAME);
        this.bottles = caskData.bottles();
        this.effects = caskData.effects();
        this.durations = caskData.durations();
        this.amplifiers = caskData.amplifiers();
        this.dilutions = caskData.dilutions();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        var caskData = feature().registers.data.get();

        builder.set(DataComponents.CUSTOM_NAME, this.name);
        builder.set(caskData, new CaskContentsData(
            this.bottles,
            this.effects,
            this.durations,
            this.amplifiers,
            this.dilutions
        ));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return bottles == 0;
    }

    public boolean isFull() {
        return bottles == CASKS.maxBottles();
    }

    @Override
    public ItemStack getItem(int i) {
        if (i >= 0 && i < items.size()) {
            return items.get(i);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(items, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(items, i);
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        if (i >= 0 && i < items.size()) {
            items.set(i, stack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return SLOTS_FOR_UP;
        }
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }
        return new int[]{};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return items.get(slot).isEmpty() && canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return !items.get(slot).isEmpty();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        var handlers = feature().handlers;

        if (!items.get(0).isEmpty() || !items.get(1).isEmpty()) {
            // Don't add items if there's stuff still in the queue.
            return false;
        }

        if (stack.is(Items.GLASS_BOTTLE) && !isEmpty()) {
            return true;
        }

        if (handlers.isValidPotion(stack) && !isFull()) {
            return true;
        }

        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public Component getName() {
        if (name != null) {
            return name;
        }
        return this.getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    Component getDefaultName() {
        return Component.translatable("container.charm.cask");
    }

    @Override
    public Class<Casks> typeForFeature() {
        return Casks.class;
    }
}
