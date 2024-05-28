package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.block.entity.CharmBlockEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public class CookingPotBlockEntity extends CharmBlockEntity<CookingPots> implements Container, WorldlyContainer {
    private static final CookingPots COOKING_POTS = Resolve.feature(CookingPots.class);
    private static final String HUNGER_TAG = "hunger";
    private static final String SATURATION_TAG = "saturation";
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1};

    // This is a queue to hold items for processing that are fed into and out of the pot via hoppers.
    public final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    private int hunger = 0;
    private float saturation = 0.0f;

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(Resolve.feature(CookingPots.class).registers.blockEntity.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity pot) {
        COOKING_POTS.handlers.hopperAddToPot(pot);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        hunger = tag.getInt(HUNGER_TAG);
        saturation = tag.getFloat(SATURATION_TAG);

        ContainerHelper.loadAllItems(tag, items, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        tag.putInt(HUNGER_TAG, hunger);
        tag.putDouble(SATURATION_TAG, saturation);

        ContainerHelper.saveAllItems(tag, items, provider);
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
            for (int i = 1; i < 3; i++) {
                var pos = getBlockPos().relative(Direction.DOWN, i);
                if (feature().handlers.isValidHeatSource(level.getBlockState(pos))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return feature().handlers.isEmpty(getBlockState());
    }

    public boolean isFull() {
        return feature().handlers.isFull(getBlockState());
    }

    public boolean add(ItemStack input) {
        if (!feature().handlers.isFood(input) || !canAddFood()) {
            return false;
        }

        var food = Objects.requireNonNull(input.get(DataComponents.FOOD));

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
                    feature().log().dev("ItemStack: " + input);
                    feature().log().dev("FoodProperties: " + food);
                    feature().log().dev("Hunger to add: " + hunger);
                    feature().log().dev("Pot hunger now at: " + this.hunger);
                    feature().log().dev("Saturation to add: " + saturation);
                    feature().log().dev("Pot saturation now at: " + this.saturation);
                }

                if (hasFinishedCooking()) {
                    state = state.setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.COOKED);
                } else {
                    state = state.setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.HAS_SOME_FOOD);
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

    public boolean fillWithBottle() {
        var level = getLevel();
        var pos = getBlockPos();
        var state = getBlockState().setValue(CookingPotBlock.PORTIONS,
            getBlockState().getValue(CookingPotBlock.PORTIONS) + 1);

        if (state.getValue(CookingPotBlock.PORTIONS) == Handlers.MAX_PORTIONS) {
            state = state.setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.FILLED_WITH_WATER);
        }

        if (level == null) {
            return false;
        }

        level.setBlock(pos, state, 2);
        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        return true;
    }

    public boolean fillWithBucket() {
        var level = getLevel();
        var pos = getBlockPos();
        var state = getBlockState()
            .setValue(CookingPotBlock.PORTIONS, feature().handlers.getMaxPortions())
            .setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.FILLED_WITH_WATER);

        if (level == null) {
            return false;
        }

        level.setBlock(getBlockPos(), state, 2);
        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        return true;
    }

    private void flush() {
        if (level == null) return;

        this.hunger = 0;
        this.saturation = 0;

        var pos = getBlockPos();
        var state = getBlockState();
        state = state
            .setValue(CookingPotBlock.PORTIONS, 0)
            .setValue(CookingPotBlock.COOKING_STATUS, CookingStatus.EMPTY);

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

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        var handlers = feature().handlers;

        if (!items.get(0).isEmpty() || !items.get(1).isEmpty()) {
            // Don't add any items if there's stuff stuck in the queue.
            return false;
        }

        if ((handlers.isWaterBucket(stack) || handlers.isWaterBottle(stack)) && canAddWater()) {
            return true;
        }

        if (handlers.isFood(stack) && canAddFood()) {
            return true;
        }

        if (stack.is(Items.BOWL) && hasFinishedCooking()) {
            return true;
        }

        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
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
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < items.size()) {
            return items.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int split) {
        return ContainerHelper.removeItem(items, slot, split);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < items.size()) {
            items.set(slot, stack);
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return SLOTS_FOR_UP;
        }
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }

        return SLOTS_FOR_SIDES;
    }

    @Override
    public Class<CookingPots> typeForFeature() {
        return CookingPots.class;
    }
}
