package svenhjol.charm.smithing.module;

import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.smithing.compat.QuarkTallow;
import svenhjol.charm.tweaks.module.NoAnvilMinimumXp;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.SMITHING, hasSubscriptions = true)
public class TallowIncreasesDurability extends MesonModule
{
    private QuarkTallow quarkTallow;
    public static List<Class<? extends Item>> repairable = new ArrayList<>();

    @Config public static int xpCost = 0;
    @Config public static double chanceOfCost = 0.75D;
    @Config public static double amountRepaired = 0.02D;

    @Override
    public void init()
    {
        repairable = Arrays.asList(
            ToolItem.class,
            SwordItem.class,
            HoeItem.class,
            ShieldItem.class,
            ArmorItem.class,
            ElytraItem.class,
            ShearsItem.class,
            FlintAndSteelItem.class,
            FishingRodItem.class
        );

        try {
            if (ForgeHelper.isModLoaded("quark")) {
                quarkTallow = QuarkTallow.class.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading QuarkTallow");
        }

        // if the zero XP feature isn't enabled then set it to 1
        if (!Charm.loader.hasModule(NoAnvilMinimumXp.class) && xpCost == 0) xpCost = 1;
    }

    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && ForgeHelper.isModLoaded("quark");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        if (quarkTallow != null) {
            quarkTallow.onAnvilUpdate(event);
        }
    }
}
