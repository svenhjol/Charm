package svenhjol.charm.tweaks.module;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Potions can stack (up to 16).")
public class StackablePotions extends MesonModule
{
    public static int size = 16;
    public static ResourceLocation POTION_ID = new ResourceLocation("potion");
    public static ResourceLocation SPLASH_ID = new ResourceLocation("splash_potion");
    public static ResourceLocation LINGERING_ID = new ResourceLocation("lingering_potion");
    public static PotionItem potion;
    public static SplashPotionItem splash;
    public static LingeringPotionItem lingering;

    @Override
    public void init()
    {
        if (isEnabled()) {
            potion = new PotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
            splash = new SplashPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
            lingering = new LingeringPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));

            // re-register dispenser splash behavior
            IDispenseItemBehavior splashBehavior = DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.get(Items.SPLASH_POTION); // via AT
            DispenserBlock.registerDispenseBehavior(splash, splashBehavior);

            OverrideHandler.changeVanillaItem(potion, POTION_ID);
            OverrideHandler.changeVanillaItem(splash, SPLASH_ID);
            OverrideHandler.changeVanillaItem(lingering, LINGERING_ID);
        }
    }
}
