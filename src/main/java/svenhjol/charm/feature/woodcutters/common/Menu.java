package svenhjol.charm.feature.woodcutters.common;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.feature.woodcutting.common.Recipe;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Much copypasta from {@link StonecutterMenu}.
 */
public class Menu extends AbstractContainerMenu {
    private static final Woodcutters WOODCUTTERS = Resolve.feature(Woodcutters.class);
    private static final Woodcutting WOODCUTTING = Resolve.feature(Woodcutting.class);

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<RecipeHolder<Recipe>> recipes;
    private ItemStack inputStack;
    private long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {};
    public final Container container = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            Menu.this.slotsChanged(this);
            Menu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    public Menu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public Menu(int syncId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(WOODCUTTERS.registers.menu.get(), syncId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = Lists.newArrayList();
        this.inputStack = ItemStack.EMPTY;
        this.access = access;
        this.level = playerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                Menu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack itemStack = Menu.this.inputSlot.remove(1);
                if (!itemStack.isEmpty()) {
                    Menu.this.setupResultSlot();
                }

                access.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (Menu.this.lastSoundTime != l) {
                        level.playSound(null, blockPos, WOODCUTTERS.registers.useSound.get(), SoundSource.BLOCKS, 0.4f, 1.0f);
                        Menu.this.lastSoundTime = l;
                    }
                });
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(Menu.this.inputSlot.getItem());
            }
        });

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<RecipeHolder<Recipe>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, WOODCUTTERS.registers.block.get());
    }

    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int i) {
        return i >= 0 && i < this.recipes.size();
    }

    @Override
    public void slotsChanged(Container inventory) {
        var itemStack = this.inputSlot.getItem();
        if (itemStack.getItem() != this.inputStack.getItem()) {
            this.inputStack = itemStack.copy();
            this.setupRecipeList(inventory, itemStack);
        }
    }

    private static SingleRecipeInput createRecipeInput(Container container) {
        return new SingleRecipeInput(container.getItem(0));
    }

    private void setupRecipeList(Container container, ItemStack stack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.recipes = this.getRecipesFor(this.level.getRecipeManager(), WOODCUTTING.registers.recipeType.get(),
                createRecipeInput(container), this.level);
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            var recipeHolder = this.recipes.get(this.selectedRecipeIndex.get());
            var stack = recipeHolder.value()
                .assemble(createRecipeInput(this.container), this.level.registryAccess());

            if (stack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(recipeHolder);
                this.resultSlot.set(stack);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }

        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return WOODCUTTERS.registers.menu.get();
    }

    public void setSlotUpdateListener(Runnable runnable) {
        this.slotUpdateListener = runnable;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = (Slot)this.slots.get(index);
        if (slot.hasItem()) {
            var itemStack2 = slot.getItem();
            var item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 1) {
                item.onCraftedBy(itemStack2, player.level(), player);
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index == 0) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(WOODCUTTING.registers.recipeType.get(),
                new SingleRecipeInput(itemStack2), this.level).isPresent()) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.moveItemStackTo(itemStack2, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemStack2, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
            this.broadcastChanges();
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.container));
    }

    /**
     * This alphabetically sorts only the registered item path, not the namespace + path.
     * @see RecipeManager#getRecipesFor(RecipeType, RecipeInput, Level)
     */
    private <I extends RecipeInput, T extends net.minecraft.world.item.crafting.Recipe<I>> List<RecipeHolder<T>> getRecipesFor(
        RecipeManager recipeManager, RecipeType<T> recipeType, I recipeInput, Level level) {
        return recipeManager.byType(recipeType).stream()
            .filter(recipeHolder -> recipeHolder.value().matches(recipeInput, level))
            .sorted(Comparator.comparing(recipeHolder -> {
                var stack = recipeHolder.value().getResultItem(level.registryAccess());
                var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                return id.getPath();
            }))
            .collect(Collectors.toList());
    }
}
