package svenhjol.charm.module.extract_enchantments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.grindable_horse_armor.GrindableHorseArmor;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@CommonModule(mod = Charm.MOD_ID, description = "Extract enchantments from any enchanted item into an empty book using the grindstone.")
public class ExtractEnchantments extends CharmModule {
    public static final ResourceLocation TRIGGER_EXTRACTED_ENCHANTMENT = new ResourceLocation(Charm.MOD_ID, "extracted_enchantment");

    @Config(name = "Initial XP cost", description = "Initial XP cost before adding XP equivalent to the enchantment level(s) of the item.")
    public static int initialCost = 2;

    @Override
    public void register() {
        this.addDependencyCheck(module -> !ModHelper.isLoaded("grindenchantments"));
    }

    public static Slot getGrindstoneInputSlot(int index, Container inputs) {
        return new Slot(inputs, index, 49, 19 + (index * 21)) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                boolean valid = stack.isDamageableItem() || stack.getItem() == Items.ENCHANTED_BOOK || stack.isEnchanted();

                // check for horse armor extraction
                if (Charm.LOADER.isEnabled("charm:grindable_horse_armor") && GrindableHorseArmor.horseArmorRecipes.containsKey(stack.getItem())) {
                    return true;
                }

                return isExtractEnchantmentsEnabled() ? valid || stack.getItem() == Items.BOOK : valid;
            }
        };
    }

    public static Slot getGrindstoneOutputSlot(ContainerLevelAccess context, Container inputs, Container output) {

        /**
         * Copypasta from GrindstoneScreenHandler 52-100 with Charm changes as marked.
         */
        return new Slot(output, 2, 129, 34) {

            /** vanilla **/
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            /**
             * Charm: override canTakeItems to check for extraction.
             * @param player Player
             * @return True if can take from output slot
             */
            public boolean mayPickup(Player player) {
                if (!isExtractEnchantmentsEnabled())
                    return true;

                List<ItemStack> stacks = getStacksFromInventory(container);

                if (shouldExtract(stacks)) {
                    int cost = getEnchantedItemFromStacks(stacks).map(ExtractEnchantments::getCost).orElse(0);
                    return hasEnoughXp(player, cost);
                }

                return true;
            }

            public void onTake(Player player, ItemStack stack) {
                ItemStack out = tryGetEnchantedBook(inputs, player);

                context.execute((world, blockPos) -> {
                    if (inputs.getItem(0).getItem() instanceof HorseArmorItem || inputs.getItem(1).getItem() instanceof HorseArmorItem) {
                        // handle advancement for grindable horse armor
                        if (!world.isClientSide)
                            GrindableHorseArmor.triggerRecycledHorseArmor((ServerPlayer) player);
                    }
                    else if (out != null && out.getItem() instanceof EnchantedBookItem) {
                        // deduct XP from player for extract enchantments
                        if (!PlayerHelper.getAbilities(player).instabuild) {
                            int cost = getCost(stack);
                            player.giveExperienceLevels(-cost);
                        }

                        // handle advancement for extract enchantments
                        if (!world.isClientSide)
                            ExtractEnchantments.triggerExtractedEnchantment((ServerPlayer) player);

                    } else {
                        /** vanilla */
                        int i = this.getExperience(world);

                        while (i > 0) {
                            int j = ExperienceOrb.getExperienceValue(i);
                            i -= j;
                            world.addFreshEntity(new ExperienceOrb(world, (double) blockPos.getX(), (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, j));
                        }
                    }
                    world.levelEvent(1042, blockPos, 0);
                });

                // ---- CHARM: SNIP ----
                ItemStack slot0 = inputs.getItem(0);
                ItemStack slot1 = inputs.getItem(1);

                if (slot0.getCount() > 1) {
                    slot0.shrink(1);
                } else if (slot1.getCount() > 1) {
                    slot1.shrink(1);
                }

                if (slot0.getCount() <= 1)
                    inputs.setItem(0, ItemStack.EMPTY);

                if (slot1.getCount() <= 1)
                    inputs.setItem(1, ItemStack.EMPTY);
                // ---- CHARM: SNIP ----
            }

            /** vanilla */
            private int getExperience(Level world) {
                int ix = 0;
                int i = ix + this.getExperience(inputs.getItem(0));
                i += this.getExperience(inputs.getItem(1));
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
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                Iterator var4 = map.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<Enchantment, Integer> entry = (Map.Entry)var4.next();
                    Enchantment enchantment = (Enchantment)entry.getKey();
                    Integer integer = (Integer)entry.getValue();
                    if (!enchantment.isCurse()) {
                        i += enchantment.getMinCost(integer);
                    }
                }

                return i;
            }
        };
    }

    public static boolean tryUpdateGrindstoneOutput(Container inputs, Container output, @Nullable Player player) {
        if (!isExtractEnchantmentsEnabled())
            return false;

        ItemStack out = tryGetEnchantedBook(inputs, player);
        if (out == null)
            return false;

        output.setItem(0, out);
        return true;
    }

    @Nullable
    private static ItemStack tryGetEnchantedBook(Container inputs, @Nullable Player player) {
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
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(in);
        enchantments.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentInstance(e, level)));
        return out;
    }

    private static boolean isExtractEnchantmentsEnabled() {
        return Charm.LOADER.isEnabled(ExtractEnchantments.class);
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
            if (ench == null)
                return 0;

            int level = entry.getValue();
            if (level > 0 && ench.isTradeable())
                cost += level;
        }

        // add repair cost on the input item
        if (stack.getTag() != null && !stack.getTag().isEmpty())
            cost += stack.getTag().getInt("RepairCost");

        return cost;
    }

    public static void triggerExtractedEnchantment(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_EXTRACTED_ENCHANTMENT);
    }
}
