package svenhjol.charm.smithing.feature;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.loot.feature.TotemOfReturning;
import svenhjol.charm.loot.item.ItemTotemOfReturning;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExtendTotemOfReturning extends Feature
{
    public static int xpCost;
    public static Map<String, Integer> useItemMap = new HashMap<>();

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

        String[] items = propStringList("Items and uses",
                "A map of items and the amount of uses they add to the totem.\n" +
                    "Format is 'modid:name[meta]->uses'.  If meta is not set, or '*', all meta values of the item will be used.",
                new String[] {
                        "minecraft:clock->1",
                        "minecraft:sapling[2]->2"
                });

        for (String line : items) {
            String[] split = line.split("->");
            if (split.length != 2) continue;
            useItemMap.put(split[0], Integer.parseInt(split[1]));
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        if (!Charm.hasFeature(TotemOfReturning.class)) return;

        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (!in.isEmpty() && !combine.isEmpty() && in.getItem() == TotemOfReturning.totem) {
            String name = ItemHelper.getMatchingItemKey(new ArrayList<>(useItemMap.keySet()), combine);
            if (!name.isEmpty()) {
                int amount = useItemMap.get(name);

                ItemStack out = in.copy();
                int uses = ItemTotemOfReturning.getUses(out);
                ItemTotemOfReturning.setUses(out, uses + amount);

                event.setOutput(out);
                event.setMaterialCost(1);
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
