package svenhjol.charm.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Extract enchantments from any enchanted item into an empty book.")
public class ExtractEnchantments extends CharmModule {
    @Config(name = "Initial XP cost", description = "Initial XP cost before adding XP equivalent to the enchantment level(s) of the item.")
    public static int initialCost = 2;

    @Config(name = "Treasure XP cost", description = "If the enchantment is a treasure enchantment, such as Mending, this cost will be added.")
    public static int treasureCost = 15;

    @Override
    public void init() {
        UpdateAnvilCallback.EVENT.register(this::tryExtract);
    }

    private ActionResult tryExtract(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (right.getItem() != Items.BOOK)
            return ActionResult.PASS;

        ListTag leftTags = left.getEnchantments();
        ListTag rightTags = right.getEnchantments();
        if (leftTags.isEmpty() || !rightTags.isEmpty())
            return ActionResult.PASS;

        int cost = initialCost;

        Map<Enchantment, Integer> inEnchants = EnchantmentHelper.get(left);
        Map<Enchantment, Integer> outEnchants = new HashMap<>();

        // get all enchantments from the left item and create a map of enchantments for the output
        for (Map.Entry<Enchantment, Integer> entry : inEnchants.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench == null)
                return ActionResult.PASS;

            int level = entry.getValue();
            if (level > 0 && ench.isAvailableForEnchantedBookOffer()) {
                outEnchants.put(ench, level);
                cost += level;

                if (ench.isTreasure())
                    cost += treasureCost;
            }
        }

        if (outEnchants.values().size() == 0)
            return ActionResult.PASS;

        // add repair cost on the input item
        if (left.getTag() != null && !left.getTag().isEmpty())
            cost += left.getTag().getInt("RepairCost");

        // apply enchantments to the book
        out = new ItemStack(Items.ENCHANTED_BOOK);
        outEnchants.forEach((e, level) -> EnchantedBookItem.addEnchantment(out, new EnchantmentLevelEntry(e, level)));

        // set the display name on the returned item
        if (name != null && !name.isEmpty())
            out.setCustomName(new LiteralText(name));

        apply.accept(out, cost, 1);
        return ActionResult.SUCCESS;
    }
}
