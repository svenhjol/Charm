package svenhjol.charm.feature.grindstone_disenchanting.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchanting;
import svenhjol.charm.foundation.feature.FeatureHolder;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Handlers extends FeatureHolder<GrindstoneDisenchanting> {
    public Handlers(GrindstoneDisenchanting feature) {
        super(feature);
    }

    public InteractionResult canTakeFromGrindstone(GrindstoneEvents.GrindstoneMenuInstance instance, Player player) {
        var stacks = getStacksFromInventory(instance.output);

        if (shouldExtract(stacks)) {
            var cost = getEnchantedItemFromStacks(stacks)
                .map(this::getCost)
                .orElse(0);

            if (hasEnoughXp(player, cost)) {
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean canPlaceOnGrindstone(Container container, ItemStack itemStack) {
        return itemStack.is(Items.BOOK);
    }

    public boolean onTakeFromGrindstone(GrindstoneEvents.GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        var out = tryGetEnchantedBook(instance.input, player);
        if (out == null) return false;

        instance.access.execute((level, pos) -> {
            if (out.getItem() instanceof EnchantedBookItem) {
                if (!player.getAbilities().instabuild) {
                    int cost = getCost(stack);
                    player.giveExperienceLevels(-cost);
                }
                feature().advancements.extractedEnchantment(player);
            }
            level.levelEvent(1042, pos, 0);
        });

        var slot0 = instance.input.getItem(0);
        var slot1 = instance.input.getItem(1);

        if (slot0.getCount() > 1) {
            slot0.shrink(1);
        } else if (slot1.getCount() > 1) {
            slot1.shrink(1);
        }

        if (slot0.getCount() <= 1) {
            instance.input.setItem(0, ItemStack.EMPTY);
        }

        if (slot1.getCount() <= 1) {
            instance.input.setItem(1, ItemStack.EMPTY);
        }

        instance.menu.broadcastChanges();
        return true;
    }

    public boolean calculateGrindstoneOutput(GrindstoneEvents.GrindstoneMenuInstance instance) {
        var out = tryGetEnchantedBook(instance.input, instance.player);
        if (out == null) return false;

        instance.output.setItem(0, out);
        return true;
    }

    public boolean shouldExtract(List<ItemStack> stacks) {
        return getEnchantedItemFromStacks(stacks).isPresent() &&
            stacks.stream().anyMatch(i -> i.is(Items.BOOK));
    }

    public Optional<ItemStack> getEnchantedItemFromStacks(List<ItemStack> stacks) {
        return stacks.stream().filter(ItemStack::isEnchanted).findFirst();
    }

    public List<ItemStack> getStacksFromInventory(Container inventory) {
        return Arrays.asList(inventory.getItem(0), inventory.getItem(1));
    }

    @Nullable
    public ItemStack tryGetEnchantedBook(Container inputs, @Nullable Player player) {
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
                var enchantment = entry.getKey();
                var power = entry.getIntValue();
                mutable.set(enchantment, mutable.getLevel(enchantment) + power);
            }));

        return out;
    }

    public boolean hasEnoughXp(Player player, int cost) {
        return player.getAbilities().instabuild || player.experienceLevel >= cost;
    }

    public int getCost(ItemStack stack) {
        var cost = GrindstoneDisenchanting.initialCost;
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        // Get all enchantments from the left item and create a map of enchantments for the output
        for (var entry : enchantments.entrySet()) {
            var enchantment = entry.getKey();

            var level = entry.getIntValue();
            if (level > 0 && enchantment.is(EnchantmentTags.TRADEABLE)) {
                cost += level;
            }

            if (enchantment.is(EnchantmentTags.TREASURE)) {
                cost += GrindstoneDisenchanting.treasureCost;
            }
        }

        // Add repair cost on the input item
        if (GrindstoneDisenchanting.addRepairCost) {
            cost += stack.getOrDefault(DataComponents.REPAIR_COST, 0);
        }

        return cost;
    }
}
