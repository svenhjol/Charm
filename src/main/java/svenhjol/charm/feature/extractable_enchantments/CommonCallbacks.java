package svenhjol.charm.feature.extractable_enchantments;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommonCallbacks {
    static boolean shouldExtract(List<ItemStack> stacks) {
        return getEnchantedItemFromStacks(stacks).isPresent() &&
            stacks.stream().anyMatch(i -> i.is(Items.BOOK));
    }

    static Optional<ItemStack> getEnchantedItemFromStacks(List<ItemStack> stacks) {
        return stacks.stream().filter(ItemStack::isEnchanted).findFirst();
    }

    static List<ItemStack> getStacksFromInventory(Container inventory) {
        return Arrays.asList(inventory.getItem(0), inventory.getItem(1));
    }

    @Nullable
    static ItemStack tryGetEnchantedBook(Container inputs, @Nullable Player player) {
        List<ItemStack> stacks = getStacksFromInventory(inputs);
        if (!shouldExtract(stacks)) return null;

        var enchanted = getEnchantedItemFromStacks(stacks);
        if (enchanted.isEmpty()) return null;

        var in = enchanted.get();
        if (player != null && !hasEnoughXp(player, getCost(in))) return null;

        var out = new ItemStack(Items.ENCHANTED_BOOK);
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(in);

        EnchantmentHelper.updateEnchantments(out, mutable -> enchantments.entrySet().forEach(
            entry -> {
                var enchantment = entry.getKey().value();
                var power = entry.getIntValue();
                mutable.set(enchantment, mutable.getLevel(enchantment) + power);
            }));

        return out;
    }

    static boolean hasEnoughXp(Player player, int cost) {
        return player.getAbilities().instabuild || player.experienceLevel >= cost;
    }

    static int getCost(ItemStack stack) {
        var cost = ExtractableEnchantments.initialCost;
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        // Get all enchantments from the left item and create a map of enchantments for the output
        for (var entry : enchantments.entrySet()) {
            var enchantment = entry.getKey().value();

            var level = entry.getIntValue();
            if (level > 0 && enchantment.isTradeable()) {
                cost += level;
            }

            if (enchantment.isTreasureOnly()) {
                cost += ExtractableEnchantments.treasureCost;
            }
        }

        // Add repair cost on the input item
        if (ExtractableEnchantments.addRepairCost) {
            cost += stack.getOrDefault(DataComponents.REPAIR_COST, 0);
        }

        return cost;
    }
}
