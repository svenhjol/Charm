package svenhjol.charm.handler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public class InventoryTidyingHandler {
    public static final int BE = 0;
    public static final int PLAYER = 1;

    private static final Map<Predicate<ItemStack>, Comparator<ItemStack>> testCompare = new HashMap<>();

    public static void init() {
        testCompare.clear();
        testCompare.put(clazzTest(BlockItem.class).negate(), anyCompare());
        testCompare.put(clazzTest(BlockItem.class), blockCompare());
    }

    public static void sort(Inventory inventory, int startSlot, int endSlot) {
        List<ItemStack> stacks = new ArrayList<>();

        populate(inventory, stacks, startSlot, endSlot);
        mergeInventory(stacks);
        sortInventory(stacks);
        setInventory(inventory, stacks, startSlot, endSlot); // TODO handle fail here
    }

    public static void populate(Inventory inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stackInSlot = inventory.getStack(i);

            if (!stackInSlot.isEmpty())
                stacks.add(stackInSlot.copy());
        }
    }

    /**
     * Core merging code adapted from Quark's SortingHandler.
     * @param stacks Inventory stack to merge within
     */
    public static void mergeInventory(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack.isEmpty())
                continue;

            for (int j = 0; j < stacks.size(); j++) {
                if (i == j)
                    continue;

                ItemStack stack1 = stacks.get(j);
                if (stack1.isEmpty())
                    continue;

                if (stack1.getCount() < stack1.getMaxCount()
                    && ItemStack.areItemsEqual(stack, stack1)
                    && ItemStack.areTagsEqual(stack, stack1)
                ) {
                    int setSize = stack1.getCount() + stack.getCount();
                    int carryover = Math.max(0, setSize - stack1.getMaxCount());
                    stack1.setCount(carryover);
                    stack.setCount(setSize - carryover);

                    if (stack.getCount() == stack.getMaxCount())
                        break;
                }
            }

            stacks.set(i, stack);
        }

        stacks.removeIf((ItemStack stack) -> stack.isEmpty() || stack.getCount() == 0);
    }

    public static void sortInventory(List<ItemStack> stacks) {
        stacks.sort(InventoryTidyingHandler::compare); // TODO world's crappiest sorting
    }

    private static boolean setInventory(Inventory inventory, List<ItemStack> stacks, int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            int j = i - startSlot;
            ItemStack stack = j >= stacks.size() ? ItemStack.EMPTY : stacks.get(j);
            inventory.removeStack(i, inventory.getMaxCountPerStack());
            if (!stack.isEmpty()) {
                // this ended up being different from Forge's IItemHandler implementation.
                // TODO May cause breakage... testme
                inventory.setStack(i, stack);
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

        for (Predicate<ItemStack> predicate : testCompare.keySet()) {
            if (predicate.test(stack1))
                index1 = index;
            if (predicate.test(stack2))
                index2 = index;

            if (index1 >= 0 && index1 == index2)
                return testCompare.get(predicate).compare(stack1, stack2);

            index++;
        }

        return index1 - index2;
    }

    private static Comparator<ItemStack> blockCompare() {
        return compare(Comparator.comparing(s -> Item.getRawId(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    private static Comparator<ItemStack> anyCompare() {
        return compare(Comparator.comparing(s -> Item.getRawId(s.getItem())),
            (ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
            (ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
    }

    @SafeVarargs
    private static Comparator<ItemStack> compare(Comparator<ItemStack>... comparators) {
        return ((stack1, stack2) -> {
            for (Comparator<ItemStack> comparator : comparators) {
                int res = comparator.compare(stack1, stack2);
                if (res != 0)
                    return res;
            }
            return 0;
        });
    }

    private static Predicate<ItemStack> clazzTest(Class<? extends Item> clazz) {
        return stack -> !stack.isEmpty() && clazz.isInstance(stack.getItem());
    }
}
