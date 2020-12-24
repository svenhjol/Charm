package svenhjol.charm.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.ModHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.ExtractEnchantmentsClient;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Module(mod = Charm.MOD_ID, client = ExtractEnchantmentsClient.class, description = "Extract enchantments from any enchanted item into an empty book using the grindstone.")
public class ExtractEnchantments extends CharmModule {
    @Config(name = "Initial XP cost", description = "Initial XP cost before adding XP equivalent to the enchantment level(s) of the item.")
    public static int initialCost = 2;

    @Override
    public boolean depends() {
        return !ModHelper.isLoaded("grindenchantments");
    }

    public static Slot getGrindstoneInputSlot(int index, Inventory inputs) {
        return new Slot(inputs, index, 49, 19 + (index * 21)) {
            @Override
            public boolean canInsert(ItemStack stack) {
                boolean valid = stack.isDamageable() || stack.getItem() == Items.ENCHANTED_BOOK || stack.hasEnchantments();
                return isEnabled() ? valid || stack.getItem() == Items.BOOK : valid;
            }
        };
    }

    public static Slot getGrindstoneOutputSlot(ScreenHandlerContext context, Inventory inputs, Inventory output) {

        /**
         * Copypasta from GrindstoneScreenHandler 52-100 with Charm changes as marked.
         */
        return new Slot(output, 2, 129, 34) {

            /** vanilla **/
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            /**
             * Charm: override canTakeItems to check for extraction.
             * @param player Player
             * @return True if can take from output slot
             */
            public boolean canTakeItems(PlayerEntity player) {
                if (!isEnabled())
                    return true;

                List<ItemStack> stacks = getStacksFromInventory(inventory);

                if (shouldExtract(stacks)) {
                    int cost = getEnchantedItemFromStacks(stacks).map(ExtractEnchantments::getCost).orElse(0);
                    return hasEnoughXp(player, cost);
                }

                return true;
            }

            public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
                ItemStack out = tryGetEnchantedBook(inputs, player);

                context.run((world, blockPos) -> {
                    if (out != null && out.getItem() instanceof EnchantedBookItem) {

                        if (!player.abilities.creativeMode) {
                            int cost = getCost(out);
                            player.addExperienceLevels(-cost);
                        }

                    } else {
                        /** vanilla */
                        int i = this.getExperience(world);

                        while (i > 0) {
                            int j = ExperienceOrbEntity.roundToOrbSize(i);
                            i -= j;
                            world.spawnEntity(new ExperienceOrbEntity(world, (double) blockPos.getX(), (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, j));
                        }
                    }
                    world.syncWorldEvent(1042, blockPos, 0);
                });

                // ---- CHARM: SNIP ----
                ItemStack slot0 = inputs.getStack(0);
                ItemStack slot1 = inputs.getStack(1);

                if (slot0.getCount() > 1) {
                    slot0.decrement(1);
                } else if (slot1.getCount() > 1) {
                    slot1.decrement(1);
                }

                if (slot0.getCount() <= 1)
                    inputs.setStack(0, ItemStack.EMPTY);

                if (slot1.getCount() <= 1)
                    inputs.setStack(1, ItemStack.EMPTY);
                // ---- CHARM: SNIP ----

                return out;
            }

            /** vanilla */
            private int getExperience(World world) {
                int ix = 0;
                int i = ix + this.getExperience(inputs.getStack(0));
                i += this.getExperience(inputs.getStack(1));
                if (i > 0) {
                    int j = (int)Math.ceil((double)i / 2.0D);
                    return j + world.random.nextInt(j);
                } else {
                    return 0;
                }
            }

            /** vanilla */
            private int getExperience(ItemStack stack) {
                int i = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.get(stack);
                Iterator var4 = map.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<Enchantment, Integer> entry = (Map.Entry)var4.next();
                    Enchantment enchantment = (Enchantment)entry.getKey();
                    Integer integer = (Integer)entry.getValue();
                    if (!enchantment.isCursed()) {
                        i += enchantment.getMinPower(integer);
                    }
                }

                return i;
            }
        };
    }

    public static boolean tryUpdateResult(Inventory inputs, Inventory output, @Nullable PlayerEntity player) {
        if (!isEnabled())
            return false;

        ItemStack out = tryGetEnchantedBook(inputs, player);
        if (out == null)
            return false;

        output.setStack(0, out);
        return true;
    }

    @Nullable
    private static ItemStack tryGetEnchantedBook(Inventory inputs, @Nullable PlayerEntity player) {
        List<ItemStack> stacks = getStacksFromInventory(inputs);
        if (!shouldExtract(stacks))
            return null;

        Optional<ItemStack> enchanted = getEnchantedItemFromStacks(stacks);
        if (!enchanted.isPresent())
            return null;

        ItemStack in = enchanted.get();
        if (player != null && !hasEnoughXp(player, getCost(in)))
            return null;

        ItemStack out = new ItemStack(Items.ENCHANTED_BOOK);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(in);
        enchantments.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentLevelEntry(e, level)));
        return out;
    }

    private static boolean isEnabled() {
        return ModuleHandler.enabled(ExtractEnchantments.class);
    }

    public static List<ItemStack> getStacksFromInventory(Inventory inventory) {
        return Arrays.asList(inventory.getStack(0), inventory.getStack(1));
    }

    public static Optional<ItemStack> getEnchantedItemFromStacks(List<ItemStack> stacks) {
        return stacks.stream().filter(ItemStack::hasEnchantments).findFirst();
    }

    public static boolean shouldExtract(List<ItemStack> stacks) {
        return getEnchantedItemFromStacks(stacks).isPresent() && stacks.stream().anyMatch(i -> i.getItem() == Items.BOOK);
    }

    public static boolean hasEnoughXp(PlayerEntity player, int cost) {
        return player.abilities.creativeMode || player.experienceLevel >= cost;
    }

    public static int getCost(ItemStack stack) {
        int cost = initialCost;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);

        // get all enchantments from the left item and create a map of enchantments for the output
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench == null)
                return 0;

            int level = entry.getValue();
            if (level > 0 && ench.isAvailableForEnchantedBookOffer())
                cost += level;
        }

        // add repair cost on the input item
        if (stack.getTag() != null && !stack.getTag().isEmpty())
            cost += stack.getTag().getInt("RepairCost");

        return cost;
    }
}
