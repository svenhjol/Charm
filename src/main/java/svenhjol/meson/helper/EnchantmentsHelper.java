package svenhjol.meson.helper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnchantmentsHelper {
    public static List<Enchantment> getItemCurses(ItemStack item) {
        ListNBT tags = item.getEnchantmentTagList();
        List<Enchantment> curses = new ArrayList<>();

        if (!tags.isEmpty()) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment == null) continue;
                if (enchantment.isCurse()) {
                    curses.add(enchantment);
                }
            }
        }

        return curses;
    }

    public static List<Enchantment> getAllCurses() {
        List<Enchantment> curses = new ArrayList<>();
        ForgeRegistries.ENCHANTMENTS.getValues().forEach(enchantment -> {
            if (enchantment.isCurse())
                curses.add(enchantment);
        });
        return curses;
    }

    public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem) {
            return EnchantmentHelper.getEnchantments(stack).containsKey(enchantment);
        } else {
            return EnchantmentHelper.getEnchantmentLevel(enchantment, stack) > 0;
        }
    }

    public static void removeEnchantment(Enchantment enchantment, ItemStack stack) {
        if (hasEnchantment(enchantment, stack)) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
            enchants.remove(enchantment);
            EnchantmentHelper.setEnchantments(enchants, stack);
        }
    }

    public static void removeRandomCurse(ItemStack stack) {
        List<Enchantment> curses = getItemCurses(stack);
        if (curses.size() > 0) {
            Enchantment curseToRemove = curses.get(new Random().nextInt(curses.size()));
            removeEnchantment(curseToRemove, stack);
        }
    }
}
