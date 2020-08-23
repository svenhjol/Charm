package svenhjol.charm.module;

import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.OverrideHandler;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Allows potions to stack.")
public class StackablePotions extends MesonModule {
    @Config(name = "Stack size", description = "Maximum potion stack size.")
    public static int stackSize = 16;

    @Override
    public void init() {
        if (enabled) {
            PotionItem potionItem = new PotionItem((new Item.Settings()).maxCount(stackSize).group(ItemGroup.BREWING));
            SplashPotionItem splashPotionItem = new SplashPotionItem((new Item.Settings()).maxCount(stackSize).group(ItemGroup.BREWING));
            LingeringPotionItem lingeringPotionItem = new LingeringPotionItem((new Item.Settings()).maxCount(stackSize).group(ItemGroup.BREWING));

            // re-register dispenser splash behavior
            OverrideHandler.overrideDispenserBehavior(Items.SPLASH_POTION, splashPotionItem);

            // re-register vanilla potion items
            OverrideHandler.overrideVanillaItem(new Identifier("potion"), potionItem);
            OverrideHandler.overrideVanillaItem(new Identifier("splash_potion"), splashPotionItem);
            OverrideHandler.overrideVanillaItem(new Identifier("lingering_potion"), lingeringPotionItem);
        }
    }
}
