package svenhjol.charm.smithing.feature;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.loot.entry.TotemOfReturningEntry;
import svenhjol.charm.loot.feature.TotemOfReturning;
import svenhjol.charm.loot.item.ItemTotemOfReturning;
import svenhjol.meson.Feature;

public class ExtendTotemOfReturning extends Feature
{
    public static int xpCost;
    public static int increaseBy;
    public static int materialCost;

    @Override
    public String getDescription()
    {
        return "Combine a Totem of Returning with a clock on an anvil to increase the number of times it can be used.";
    }

    @Override
    public void setupConfig()
    {
        xpCost = propInt(
            "XP cost",
            "Amount of XP (levels) required to increase the number of times a Totem of Returning can be used.",
            5
        );

        // internal
        increaseBy = 1;
        materialCost = 1;
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        if (!Charm.hasFeature(TotemOfReturning.class)) return;

        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (!in.isEmpty() && !combine.isEmpty()) {
            if (in.getItem() == TotemOfReturning.totem && combine.getItem() == Items.CLOCK) {
                ItemStack out = in.copy();
                ItemTotemOfReturning totem = new ItemTotemOfReturning();
                TotemOfReturningEntry entry = totem.getEntry(out);

                if (entry == null) {
                    totem.setEntry(out, TotemOfReturning.numberOfUses);
                    entry = totem.getEntry(out);
                }

                totem.setEntry(out, entry.uses + increaseBy);

                event.setOutput(out);
                event.setMaterialCost(materialCost);
                event.setCost(xpCost);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
