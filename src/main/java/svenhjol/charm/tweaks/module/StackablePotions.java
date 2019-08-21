package svenhjol.charm.tweaks.module;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

@Module(category = CharmCategories.TWEAKS)
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
        potion = new PotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        splash = new SplashPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        lingering = new LingeringPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));

        potion.setRegistryName(POTION_ID);
        splash.setRegistryName(SPLASH_ID);
        lingering.setRegistryName(LINGERING_ID);

        // Vanilla registry
        Registry.register(Registry.ITEM, POTION_ID, potion);
        Registry.register(Registry.ITEM, SPLASH_ID, splash);
        Registry.register(Registry.ITEM, LINGERING_ID, lingering);

        // Forge registry
        RegistryHandler.registerItem(potion, potion.getRegistryName());
        RegistryHandler.registerItem(splash, splash.getRegistryName());
        RegistryHandler.registerItem(lingering, lingering.getRegistryName());
    }
}
