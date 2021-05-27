package svenhjol.charm.module.anvil_improvements;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

import java.util.HashMap;
import java.util.List;
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

    @Config(name = "Show item repair cost", description = "If true, items show their repair cost in their tooltip when looking at the anvil screen.")
    public static boolean showRepairCost = true;

    public static boolean removeTooExpensive() {
        return ModuleHandler.enabled("charm:anvil_improvements") && AnvilImprovements.removeTooExpensive;
    }

    public static boolean allowTakeWithoutXp(PlayerEntity player, Property levelCost) {
        return ModuleHandler.enabled("charm:anvil_improvements")
            && (PlayerHelper.getAbilities(player).creativeMode || ((player.experienceLevel >= levelCost.get()) && levelCost.get() > -1));
    }

    public static void setEnchantmentsAllowHighLevel(Map<Enchantment, Integer> enchantments, ItemStack book, ItemStack output) {
        if (book.isEmpty() || output.isEmpty())
            return;

        if (ModuleHandler.enabled(AnvilImprovements.class) && book.getItem() instanceof EnchantedBookItem) {
            Map<Enchantment, Integer> reset = new HashMap<>();
            Map<Enchantment, Integer> bookEnchants = EnchantmentHelper.get(book);

            bookEnchants.forEach((e, l) -> {
                if (l > e.getMaxLevel())
                    reset.put(e, l);
            });

            reset.forEach((e, l) -> {
                if (enchantments.containsKey(e))
                    enchantments.put(e, l);
            });
        }

        EnchantmentHelper.set(enchantments, output);
    }

    public static boolean tryDamageAnvil() {
        return ModuleHandler.enabled("charm:anvil_improvements")
            && AnvilImprovements.strongerAnvils
            && new Random().nextFloat() < 0.5F;
    }

    public static List<Text> addRepairCostToTooltip(ItemStack stack, List<Text> tooltip) {
        if (!AnvilImprovements.showRepairCost)
            return tooltip;

        int repairCost = stack.getRepairCost();
        if (repairCost > 0) {
            tooltip.add(LiteralText.EMPTY); // a new line
            tooltip.add(new TranslatableText("item.charm.repair_cost", repairCost).formatted(Formatting.GRAY));
        }

        return tooltip;
    }
}
