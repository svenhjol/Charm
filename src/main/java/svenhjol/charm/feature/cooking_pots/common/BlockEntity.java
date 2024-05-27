package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.block.entity.CharmBlockEntity;

import javax.annotation.Nullable;

public class BlockEntity extends CharmBlockEntity<CookingPots> {
    private static final String HUNGER_TAG = "hunger";
    private static final String SATURATION_TAG = "saturation";

    int hunger = 0;
    float saturation = 0.0f;

    public BlockEntity(BlockPos pos, BlockState state) {
        super(Resolve.feature(CookingPots.class).registers.blockEntity.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        hunger = tag.getInt(HUNGER_TAG);
        saturation = tag.getFloat(SATURATION_TAG);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        tag.putInt(HUNGER_TAG, hunger);
        tag.putDouble(SATURATION_TAG, saturation);
    }

    public boolean canAddFood() {
        return isFull()
            && hasFire()
            && !hasFinishedCooking();
    }

    public boolean canAddWater() {
        return !isFull() && !hasFinishedCooking();
    }

    public boolean hasFinishedCooking() {
        return hunger >= feature().handlers.getMaxHunger()
            && saturation >= feature().handlers.getMaxSaturation();
    }

    public boolean hasFire() {
        if (level != null) {
            var pos = getBlockPos().below();
            return feature().handlers.isValidHeatSource(level.getBlockState(pos));
        }
        return false;
    }

    public boolean isEmpty() {
        return feature().handlers.isEmpty(getBlockState());
    }

    public boolean isFull() {
        return feature().handlers.isFull(getBlockState());
    }

    public boolean add(ItemStack input) {
        if (!input.has(DataComponents.FOOD)) {
            return false;
        }

        if (!canAddFood()) {
            return false;
        }

        var food = input.get(DataComponents.FOOD);
        if (food == null) {
            return false;
        }

        if (this.hunger <= feature().handlers.getMaxHunger() || this.saturation <= feature().handlers.getMaxSaturation()) {
            if (level != null) {
                var pos = getBlockPos();
                var state = getBlockState();
                var random = level.getRandom();
                var hunger = food.nutrition() + random.nextInt(1);
                var saturation = food.saturation() + (random.nextFloat() * 0.1f);

                this.hunger += hunger;
                this.saturation += saturation;

                if (!level.isClientSide()) {
                    feature().log().debug("ItemStack: " + input);
                    feature().log().debug("FoodProperties: " + food);
                    feature().log().debug("Hunger to add: " + hunger);
                    feature().log().debug("Pot hunger now at: " + this.hunger);
                    feature().log().debug("Saturation to add: " + saturation);
                    feature().log().debug("Pot saturation now at: " + this.saturation);
                }

                if (hasFinishedCooking()) {
                    state = state.setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.COOKED);
                } else {
                    state = state.setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.IN_PROGRESS);
                }

                level.setBlock(pos, state, 3);

                setChanged();

                // Let nearby players know an item was added to the pot
                if (!level.isClientSide) {
                    level.playSound(null, pos, feature().registers.addSound.get(), SoundSource.BLOCKS, 0.8f, 1.0f);
                    Networking.S2CAddedToCookingPot.send((ServerLevel)level, pos);
                }

                return true;
            }
        }

        return false;
    }

    public ItemStack take() {
        if (!hasFinishedCooking() || isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (level != null) {
            var bowl = getPortion();

            removePortion();

            if (!level.isClientSide) {
                level.playSound(null, getBlockPos(), feature().registers.takeSound.get(), SoundSource.BLOCKS, 0.8f, 1.0f);
            }

            return bowl;
        }

        return ItemStack.EMPTY;
    }

    private void flush() {
        if (level == null) return;

        this.hunger = 0;
        this.saturation = 0;

        var pos = getBlockPos();
        var state = getBlockState();
        state = state
            .setValue(CookingPotBlock.PORTIONS, 0)
            .setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.NONE);

        level.setBlock(pos, state, 2);
        setChanged();
    }

    private void removePortion() {
        if (level == null) return;

        var pos = getBlockPos();
        var state = getBlockState();

        var portions = state.getValue(CookingPotBlock.PORTIONS) - 1;
        state = state.setValue(CookingPotBlock.PORTIONS, portions);

        if (portions <= 0) {
            flush();
        } else {
            level.setBlock(pos, state, 2);
            setChanged();
        }
    }

    private ItemStack getPortion() {
        return new ItemStack(feature().registers.mixedStewItem.get());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Class<CookingPots> typeForFeature() {
        return CookingPots.class;
    }
}
