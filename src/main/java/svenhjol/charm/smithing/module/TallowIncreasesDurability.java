package svenhjol.charm.smithing.module;

import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.SMITHING, hasSubscriptions = true,
    description = "Tallow can be combined on an anvil with a tool or armor to give a small durability boost.\n" +
        "Repairing using tallow has a chance to increase future repair costs of the tool or armor.\n" +
        "Quark must be installed for this feature to work.")
public class TallowIncreasesDurability extends MesonModule {
    public static List<Class<? extends Item>> repairable = new ArrayList<>();

    @Config(name = "XP cost", description = "Number of levels required to apply tallow.")
    public static int xpCost = 0;

    @Config(name = "Chance of repair cost increase", description = "Chance (out of 1.0) of the item's repair cost increasing when tallow is applied.")
    public static double chanceOfCost = 0.1D;

    @Config(name = "Amount repaired", description = "Percentage (where 1.0 = 100%) durability repaired when using tallow.")
    public static double amountRepaired = 0.02D;

    @Override
    public void init() {
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
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (!Meson.isModuleEnabled("charm:no_anvil_minimum_xp") && xpCost == 0)
            xpCost = 1;
    }

    @Override
    public boolean shouldRunSetup() {
        return ForgeHelper.isModLoaded("quark");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (Charm.quarkCompat != null)
            Charm.quarkCompat.onTallowAnvilUpdate(event);
    }
}
