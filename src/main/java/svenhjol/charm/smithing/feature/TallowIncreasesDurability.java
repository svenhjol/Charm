package svenhjol.charm.smithing.feature;

import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.smithing.compat.QuarkTallow;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.ForgeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TallowIncreasesDurability extends Feature
{
    public static List<Class<? extends Item>> repairable = new ArrayList<>();
    public static double amount;
    public static double chance;
    public static int xpCost;
    private QuarkTallow quarkTallow;

    @Override
    public String getDescription()
    {
        return "Tallow can be combined on an anvil with a tool or armor to give a small durability boost.\n" +
                "Repairing using tallow has a chance to increase future repair costs of the tool or armor.\n" +
                "NOTE: Quark must be installed for this feature to be enabled.";
    }

    @Override
    public void configure()
    {
        super.configure();

        amount = propDouble(
            "Amount repaired",
            "Percentage (out of 1.0) of the tool or armor repaired by a piece of tallow.",
            0.02D
        );
        chance = propDouble(
            "Chance of repair cost increase",
            "Chance (out of 1.0) of tallow increasing the future repair cost of the tool.",
            0.75D
        );
        xpCost = propInt(
            "XP cost",
            "Amount of XP (levels) required to repair.",
            0
        );

        // internal
        repairable = Arrays.asList(
            ItemTool.class,
            ItemSword.class,
            ItemHoe.class,
            ItemShield.class,
            ItemArmor.class,
            ItemElytra.class,
            ItemShears.class,
            ItemFlintAndSteel.class,
            ItemFishingRod.class
        );

        try {
            if (ForgeHelper.areModsLoaded("quark")) {
                quarkTallow = QuarkTallow.class.newInstance();
            }
        } catch (Exception e) {
            Meson.runtimeException("Error loading QuarkTallow");
        }
    }

    @Override
    public boolean isEnabled()
    {
        return enabled && ForgeHelper.areModsLoaded("quark");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        if (quarkTallow != null) {
            quarkTallow.onAnvilUpdate(event);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
