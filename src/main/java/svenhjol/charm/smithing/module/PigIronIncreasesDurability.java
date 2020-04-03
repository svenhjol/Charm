package svenhjol.charm.smithing.module;

import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.item.PigIronNuggetItem;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.SMITHING, hasSubscriptions = true,
    description = "Pig iron nuggets can be combined on an anvil with any tool or armor to give a small durability boost with no repair cost increase.")
public class PigIronIncreasesDurability extends MesonModule {
    public static List<Class<? extends Item>> repairable = new ArrayList<>();

    @Config(name = "XP cost", description = "Number of levels required to apply pig iron nuggets.")
    public static int xpCost = 0;

    @Config(name = "Amount repaired", description = "Percentage (where 1.0 = 100%) durability repaired when using pig iron nuggets.")
    public static double amountRepaired = 0.05D;

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
        return Meson.isModuleEnabled("charm:nether_pig_iron");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;

        Item l = left.getItem();
        Item r = right.getItem();

        if (!(r instanceof PigIronNuggetItem)) return;

        boolean repairable = l instanceof ToolItem
            || l instanceof ArmorItem
            || PigIronIncreasesDurability.repairable.contains(l.getClass());
        if (!repairable) return;

        int currentDamage = l.getDamage(left);
        int maxDamage = left.getMaxDamage();

        if (currentDamage == 0) return;

        out = left.copy();
        out.setDamage(currentDamage - (int) (maxDamage * PigIronIncreasesDurability.amountRepaired));

        event.setOutput(out);
        event.setCost(PigIronIncreasesDurability.xpCost);
        event.setMaterialCost(1);
    }
}
