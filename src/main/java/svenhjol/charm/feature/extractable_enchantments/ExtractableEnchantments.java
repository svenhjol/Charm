package svenhjol.charm.feature.extractable_enchantments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import svenhjol.charm.Charm;
import svenhjol.charmapi.event.GrindstoneEvents;
import svenhjol.charmapi.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.AdvancementHelper;
import svenhjol.charmony.helper.ConfigHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@Feature(mod = Charm.MOD_ID, description = "Extract enchantments from any enchanted item onto an empty book using the grindstone.")
public class ExtractableEnchantments extends CharmFeature {
    private static final ResourceLocation EXTRACTED_ENCHANTMENT = Charm.instance().makeId("extracted_enchantment");
    @Configurable(name = "Initial cost", description = "Initial cost (in XP levels) of extraction before adding on the cost of the enchantment(s).")
    public static int initialCost = 5;

    @Configurable(name = "Treasure cost", description = "Adds extra cost (in XP levels) if the enchantment is a treasure enchantment such as Mending.")
    public static int treasureCost = 5;

    @Configurable(name = "Add item repair cost", description = "If true, the item's repair cost will be added to the cost of extraction.")
    public static boolean addRepairCost = true;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("grindenchantments"));
    }

    @Override
    public void runWhenEnabled() {
        GrindstoneEvents.ON_TAKE.handle(this::handleOnTake);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        GrindstoneEvents.CAN_TAKE.handle(this::handleCanTake);
        GrindstoneEvents.CAN_PLACE.handle(this::handleCanPlace);
    }

    private InteractionResult handleCanTake(GrindstoneMenuInstance instance, Player player) {
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

    private boolean handleCanPlace(Container container, ItemStack itemStack) {
        return itemStack.is(Items.BOOK);
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        var out = tryGetEnchantedBook(instance.input, player);
        if (out == null) return false;

        instance.access.execute((level, pos) -> {
            if (out.getItem() instanceof EnchantedBookItem) {
                if (!player.getAbilities().instabuild) {
                    int cost = getCost(stack);
                    player.giveExperienceLevels(-cost);
                }

                if (!level.isClientSide) {
                    AdvancementHelper.trigger(EXTRACTED_ENCHANTMENT, (ServerPlayer)player);
                }
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

    boolean handleCalculateOutput(GrindstoneMenuInstance instance) {
        var out = tryGetEnchantedBook(instance.input, instance.player);
        if (out == null) return false;

        instance.output.setItem(0, out);
        return true;
    }

    Optional<ItemStack> getEnchantedItemFromStacks(List<ItemStack> stacks) {
        return stacks.stream().filter(ItemStack::isEnchanted).findFirst();
    }

    List<ItemStack> getStacksFromInventory(Container inventory) {
        return Arrays.asList(inventory.getItem(0), inventory.getItem(1));
    }

    @Nullable
    ItemStack tryGetEnchantedBook(Container inputs, @Nullable Player player) {
        List<ItemStack> stacks = getStacksFromInventory(inputs);
        if (!shouldExtract(stacks)) return null;

        var enchanted = getEnchantedItemFromStacks(stacks);
        if (enchanted.isEmpty()) return null;

        var in = enchanted.get();
        if (player != null && !hasEnoughXp(player, getCost(in))) return null;

        var out = new ItemStack(Items.ENCHANTED_BOOK);
        var enchantments = EnchantmentHelper.getEnchantments(in);
        enchantments.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentInstance(e, level)));

        return out;
    }

    boolean shouldExtract(List<ItemStack> stacks) {
        return getEnchantedItemFromStacks(stacks).isPresent() &&
            stacks.stream().anyMatch(i -> i.is(Items.BOOK));
    }

    boolean hasEnoughXp(Player player, int cost) {
        return player.getAbilities().instabuild || player.experienceLevel >= cost;
    }

    int getCost(ItemStack stack) {
        var cost = initialCost;
        var enchantments = EnchantmentHelper.getEnchantments(stack);

        // get all enchantments from the left item and create a map of enchantments for the output
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            var ench = entry.getKey();
            if (ench == null) {
                return 0;
            }

            var level = entry.getValue();
            if (level > 0 && ench.isTradeable()) {
                cost += level;
            }

            if (ench.isTreasureOnly()) {
                cost += treasureCost;
            }
        }

        // add repair cost on the input item
        if (addRepairCost && stack.getTag() != null && !stack.getTag().isEmpty()) {
            cost += stack.getTag().getInt("RepairCost");
        }

        return cost;
    }
}