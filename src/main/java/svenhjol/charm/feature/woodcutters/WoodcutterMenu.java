package svenhjol.charm.feature.woodcutters;

import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charmony.feature.woodcutting.Woodcutting;
import svenhjol.charmony.feature.woodcutting.WoodcuttingRecipe;

import java.util.List;

/**
 * Much copypasta from {@link StonecutterMenu}.
 */
public class WoodcutterMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess context;
    private final DataSlot selectedRecipe;
    private final Level level;
    private List<WoodcuttingRecipe> availableRecipes;
    private ItemStack inputStack;
    private long lastTakeTime;
    final Slot inputSlot;
    final Slot outputSlot;
    private Runnable contentsChangedListener;
    public final Container input;
    private final ResultContainer output;

    public WoodcutterMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public WoodcutterMenu(int syncId, Inventory playerInventory, final ContainerLevelAccess context) {
        super(Woodcutters.menu.get(), syncId);
        this.selectedRecipe = DataSlot.standalone();
        this.availableRecipes = Lists.newArrayList();
        this.inputStack = ItemStack.EMPTY;
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleContainer(1) {
            public void setChanged() {
                super.setChanged();
                WoodcutterMenu.this.slotsChanged(this);
                WoodcutterMenu.this.contentsChangedListener.run();
            }
        };
        this.output = new ResultContainer();
        this.context = context;
        this.level = playerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                WoodcutterMenu.this.output.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack itemStack = WoodcutterMenu.this.inputSlot.remove(1);
                if (!itemStack.isEmpty()) {
                    WoodcutterMenu.this.populateResult();
                }

                context.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (WoodcutterMenu.this.lastTakeTime != l) {
                        level.playSound(null, blockPos, Woodcutters.useSound.get(), SoundSource.BLOCKS, 0.4F, 1.0F);
                        WoodcutterMenu.this.lastTakeTime = l;
                    }
                });
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(WoodcutterMenu.this.inputSlot.getItem());
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

        this.addDataSlot(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<WoodcuttingRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasItem() && !this.availableRecipes.isEmpty();
    }

    public boolean stillValid(Player player) {
        return stillValid(this.context, player, Woodcutters.block.get());
    }

    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int i) {
        return i >= 0 && i < this.availableRecipes.size();
    }

    public void slotsChanged(Container inventory) {
        var itemStack = this.inputSlot.getItem();
        if (itemStack.getItem() != this.inputStack.getItem()) {
            this.inputStack = itemStack.copy();
            this.updateInput(inventory, itemStack);
        }

    }

    private void updateInput(Container input, ItemStack stack) {
        this.availableRecipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.set(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.availableRecipes = this.level.getRecipeManager().getRecipesFor(Woodcutting.recipeType.get(), input, this.level);
        }

    }

    private void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipe.get())) {
            var woodcuttingRecipe = this.availableRecipes.get(this.selectedRecipe.get());
            this.output.setRecipeUsed(woodcuttingRecipe);
            this.outputSlot.set(woodcuttingRecipe.assemble(this.input, this.level.registryAccess()));
        } else {
            this.outputSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return Woodcutters.menu.get();
    }

    public void setContentsChangedListener(Runnable runnable) {
        this.contentsChangedListener = runnable;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.output && super.canTakeItemForPickAll(stack, slot);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
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
            } else if (this.level.getRecipeManager().getRecipeFor(Woodcutting.recipeType.get(), new SimpleContainer(new ItemStack[]{itemStack2}), this.level).isPresent()) {
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
        this.output.removeItemNoUpdate(1);
        this.context.execute((level, blockPos) -> {
            this.clearContainer(player, this.input);
        });
    }
}
