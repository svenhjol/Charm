package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public class InventoryTidyingHandler {
    private static final Map<Predicate<ItemStack>, Comparator<ItemStack>> COMPARE = new HashMap<>();

    public static void init() {
        COMPARE.clear();
        COMPARE.put(clazzTest(BlockItem.class).negate(), anyCompare());
        COMPARE.put(clazzTest(BlockItem.class), blockCompare());
    }

    public static void sort(Container inventory, int startSlot, int endSlot) {
        var stacks = getInventoryStacks(inventory, startSlot, endSlot);

        mergeStacks(stacks);
        sortStacks(stacks);
        setInventory(inventory, stacks, startSlot, endSlot);
    }

    public static List<ItemStack> getInventoryStacks(Container inventory, int startSlot, int endSlot) {
        List<ItemStack> stacks = new ArrayList<>();

        for (int i = startSlot; i < endSlot; i++) {
            var stackInSlot = inventory.getItem(i);

            if (!stackInSlot.isEmpty()) {
                stacks.add(stackInSlot.copy());
            }
        }

        return stacks;
    }

    /**
     * Core merging code adapted from Quark's SortingHandler.
     * @param stacks Inventory stack to merge within
     */
    public static void mergeStacks(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            var stack = stacks.get(i);
            if (stack.isEmpty()) continue;

            for (var j = 0; j < stacks.size(); j++) {
                if (i == j) continue;

                var stack1 = stacks.get(j);
                if (stack1.isEmpty()) continue;

                if (stack1.getCount() < stack1.getMaxStackSize()
                    && ItemStack.isSameItemSameTags(stack, stack1)
                ) {
                    var setSize = stack1.getCount() + stack.getCount();
                    var carryover = Math.max(0, setSize - stack1.getMaxStackSize());
                    stack1.setCount(carryover);
                    stack.setCount(setSize - carryover);

                    if (stack.getCount() == stack.getMaxStackSize()) break;
                }
            }

            stacks.set(i, stack);
        }

        stacks.removeIf((ItemStack stack) -> stack.isEmpty() || stack.getCount() == 0);
    }

    public static void mergeStacks(Container container) {
        List<ItemStack> stacks = new ArrayList<>();
        var size = container.getContainerSize();
        for (var i = 0; i < size; i++) {
            stacks.add(container.getItem(i));
        }

        mergeStacks(stacks);
        container.clearContent();
        for (var i = 0; i < stacks.size(); i++) {
            container.setItem(i, stacks.get(i));
        }
    }

    public static void sortStacks(List<ItemStack> stacks) {
        stacks.sort(InventoryTidyingHandler::compare); // maybe improve this at some point in future
    }

    public static boolean setInventory(Container inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (var i = startSlot; i < endSlot; i++) {
            var j = i - startSlot;
            var stack = j >= stacks.size() ? ItemStack.EMPTY : stacks.get(j);
            inventory.removeItem(i, inventory.getMaxStackSize());
            if (!stack.isEmpty()) {
                inventory.setItem(i, stack);
            }
        }

        return true;
    }

    private static int compare(ItemStack stack1, ItemStack stack2) {
        if (stack1 == stack2) {
            return 0;
        } else if (stack1.isEmpty()) {
            return -1;
        } else if (stack2.isEmpty()) {
            return 1;
        }

        int index1 = 1, index2 = -1, index = 0;

        for (var predicate : COMPARE.keySet()) {
            if (predicate.test(stack1)) {
                index1 = index;
            }
            if (predicate.test(stack2)) {
                index2 = index;
            }

            if (index1 >= 0 && index1 == index2) {
                return COMPARE.get(predicate).compare(stack1, stack2);
            }

            index++;
        }

        return index1 - index2;
    }

    private static Comparator<ItemStack> blockCompare() {
        return compare(Comparator.comparing(s -> Item.getId(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    private static Comparator<ItemStack> anyCompare() {
        return compare(Comparator.comparing(s -> Item.getId(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    @SafeVarargs
    private static Comparator<ItemStack> compare(Comparator<ItemStack>... comparators) {
        return ((stack1, stack2) -> {
            for (Comparator<ItemStack> comparator : comparators) {
                var res = comparator.compare(stack1, stack2);
                if (res != 0) return res;
            }
            return 0;
        });
    }

    private static Predicate<ItemStack> clazzTest(Class<? extends Item> clazz) {
        return stack -> !stack.isEmpty() && clazz.isInstance(stack.getItem());
    }
}
