package svenhjol.charm.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.Map;
import java.util.Random;

@Module(mod = Charm.MOD_ID, description = "Removes minimum and maximum XP costs on the anvil. Anvils are also less likely to break.")
public class AnvilImprovements extends CharmModule {
    @Config(name = "Remove Too Expensive", description = "If true, removes the maximum cost of 40 XP when working items on the anvil.")
    public static boolean removeTooExpensive = true;

    @Config(name = "Stronger anvils", description = "If true, anvils are 50% less likely to take damage when used.")
    public static boolean strongerAnvils = true;

    @Config(name = "Allow higher enchantment levels", description = "If true, an enchanted book with a level higher than the maximum enchantment level may be applied to an item.")
    public static boolean higherEnchantmentLevels = true;

    public static boolean allowTooExpensive() {
        return ModuleHandler.enabled("charm:anvil_improvements") && AnvilImprovements.removeTooExpensive;
    }

    public static boolean allowTakeWithoutXp(PlayerEntity player, Property levelCost) {
        return ModuleHandler.enabled("charm:anvil_improvements")
            && (player.abilities.creativeMode || ((player.experienceLevel >= levelCost.get()) && levelCost.get() > -1));
    }

    public static int getEnchantmentMaxLevel(Enchantment enchantment, ItemStack stack) {
        if (ModuleHandler.enabled("charm:anvil_improvements")
            && higherEnchantmentLevels
            && stack.getItem() == Items.ENCHANTED_BOOK
        ) {
            Map<Enchantment, Integer> map = EnchantmentHelper.get(stack);
            if (map.containsKey(enchantment)) {
                int level = map.get(enchantment);
                if (level > enchantment.getMaxLevel())
                    return level;
            }
        }

        return enchantment.getMaxLevel();
    }

    public static boolean tryDamageAnvil() {
        return ModuleHandler.enabled("charm:anvil_improvements")
            && AnvilImprovements.strongerAnvils
            && new Random().nextFloat() < 0.5F;
    }
}
