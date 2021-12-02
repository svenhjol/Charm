package svenhjol.charm.module.extract_enchantments;

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
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.GrindstoneEvents;
import svenhjol.charm.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CommonModule(mod = Charm.MOD_ID, description = "Extract enchantments from any enchanted item into an empty book using the grindstone.")
public class ExtractEnchantments extends CharmModule {
    public static final ResourceLocation TRIGGER_EXTRACTED_ENCHANTMENT = new ResourceLocation(Charm.MOD_ID, "extracted_enchantment");

    @Config(name = "Initial XP cost", description = "Number of XP levels required before adding on the cost of the enchanted item.")
    public static int initialCost = 5;

    @Override
    public void register() {
        this.addDependencyCheck(module -> !ModHelper.isLoaded("grindenchantments"));
    }

    @Override
    public void runWhenEnabled() {
        GrindstoneEvents.ON_TAKE.register(this::handleOnTake);
        GrindstoneEvents.CALCULATE_OUTPUT.register(this::handleCalculateOutput);
        GrindstoneEvents.CAN_TAKE.register(this::handleCanTake);
        GrindstoneEvents.CAN_PLACE.register(this::handleCanPlace);
    }

    private boolean handleCanPlace(Container container, ItemStack stack) {
        return Charm.LOADER.isEnabled(ExtractEnchantments.class) && stack.getItem() == Items.BOOK;
    }

    private InteractionResult handleCanTake(GrindstoneMenuInstance instance, Player player) {
        if (!Charm.LOADER.isEnabled(ExtractEnchantments.class)) return InteractionResult.PASS;

        List<ItemStack> stacks = getStacksFromInventory(instance.output);
        if (shouldExtract(stacks)) {
            int cost = getEnchantedItemFromStacks(stacks)
                .map(ExtractEnchantments::getCost)
                .orElse(0);

            if (hasEnoughXp(player, cost)) {
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean handleCalculateOutput(GrindstoneMenuInstance instance) {
        if (!Charm.LOADER.isEnabled(ExtractEnchantments.class)) return false;

        ItemStack out = tryGetEnchantedBook(instance.input, instance.player);
        if (out == null) return false;

        instance.output.setItem(0, out);
        return true;
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        if (!Charm.LOADER.isEnabled(ExtractEnchantments.class)) return false;

        ItemStack out = tryGetEnchantedBook(instance.input, player);

        instance.access.execute((level, pos) -> {
            if (out != null && out.getItem() instanceof EnchantedBookItem) {
                if (!player.getAbilities().instabuild) {
                    int cost = getCost(stack);
                    player.giveExperienceLevels(-cost);
                }

                if (!level.isClientSide) {
                    ExtractEnchantments.triggerExtractedEnchantment((ServerPlayer) player);
                }
            }
            level.levelEvent(1042, pos, 0);
        });

        ItemStack slot0 = instance.input.getItem(0);
        ItemStack slot1 = instance.input.getItem(1);

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

    @Nullable
    private static ItemStack tryGetEnchantedBook(Container inputs, @Nullable Player player) {
        List<ItemStack> stacks = getStacksFromInventory(inputs);
        if (!shouldExtract(stacks)) return null;

        Optional<ItemStack> enchanted = getEnchantedItemFromStacks(stacks);
        if (enchanted.isEmpty()) return null;

        ItemStack in = enchanted.get();
        if (player != null && !hasEnoughXp(player, getCost(in))) return null;

        ItemStack out = new ItemStack(Items.ENCHANTED_BOOK);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(in);
        enchantments.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentInstance(e, level)));
        return out;
    }

    public static List<ItemStack> getStacksFromInventory(Container inventory) {
        return Arrays.asList(inventory.getItem(0), inventory.getItem(1));
    }

    public static Optional<ItemStack> getEnchantedItemFromStacks(List<ItemStack> stacks) {
        return stacks.stream().filter(ItemStack::isEnchanted).findFirst();
    }

    public static boolean shouldExtract(List<ItemStack> stacks) {
        return getEnchantedItemFromStacks(stacks).isPresent() && stacks.stream().anyMatch(i -> i.getItem() == Items.BOOK);
    }

    public static boolean hasEnoughXp(Player player, int cost) {
        return player.getAbilities().instabuild || player.experienceLevel >= cost;
    }

    public static int getCost(ItemStack stack) {
        int cost = initialCost;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        // get all enchantments from the left item and create a map of enchantments for the output
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench == null) {
                return 0;
            }

            int level = entry.getValue();
            if (level > 0 && ench.isTradeable()) {
                cost += level;
            }
        }

        // add repair cost on the input item
        if (stack.getTag() != null && !stack.getTag().isEmpty()) {
            cost += stack.getTag().getInt("RepairCost");
        }

        return cost;
    }

    public static void triggerExtractedEnchantment(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_EXTRACTED_ENCHANTMENT);
    }
}
