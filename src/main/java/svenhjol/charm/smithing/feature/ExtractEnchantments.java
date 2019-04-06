package svenhjol.charm.smithing.feature;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

import java.util.HashMap;
import java.util.Map;

/**
 * This extracts the enchantments from any enchanted item into an empty book,
 * weakening the enchantment in the process.
 */
public class ExtractEnchantments extends Feature
{
    public static int baseCost; // initial XP cost, compounded by enchantment cost
    public static int weaken; // weaken by this number of levels when converting

    @Override
    public String getDescription()
    {
        return "Extract enchantments from any enchanted item into an empty book.\n" +
                "The enchantments are weakened in the process, and the original item is destroyed.";
    }

    @Override
    public void setupConfig()
    {
        baseCost = propInt(
                "Base XP cost",
                "The minimum XP cost before adding XP equivalent to the enchantment level(s) of the item.",
                1
        );
        weaken = propInt(
                "Weaken by amount",
                "Number of levels that enchantments are weakened when converting.",
                1
        );
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (!in.isEmpty() && !combine.isEmpty() && combine.getItem() == Items.BOOK) {

            NBTTagList inTags = in.getEnchantmentTagList();
            NBTTagList combineTags = combine.getEnchantmentTagList();
            if (!inTags.isEmpty() && combineTags.isEmpty()) {
                int cost = baseCost;
                int numEnchantments = 0;

                Map<Enchantment, Integer> inEnchants = EnchantmentHelper.getEnchantments(in);
                Map<Enchantment, Integer> outEnchants = new HashMap<>();

                for (Map.Entry<Enchantment, Integer> entry : inEnchants.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    if (enchantment == null) continue;


                    int level = entry.getValue();
                    int w = level - weaken;
                    if (w > 0) {
                        numEnchantments++;
                        outEnchants.put(enchantment, w);
                        cost += w;
                    }
                }

                if (numEnchantments == 0) {
                    event.setCanceled(true);
                    return;
                }

                // add repair cost on the input item
                if (in.getTagCompound() != null && !in.getTagCompound().isEmpty()) {
                    cost += in.getTagCompound().getInteger("RepairCost");
                }

                // apply enchantments to the book
                ItemStack out = new ItemStack(Items.ENCHANTED_BOOK);
                EnchantmentHelper.setEnchantments(outEnchants, out);

                String name = event.getName();
                if (!name.isEmpty()) {
                    out.setStackDisplayName(name);
                }

                event.setCost(cost);
                event.setMaterialCost(1);
                event.setOutput(out);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}