package svenhjol.charm.tweaks.feature;

import net.minecraft.item.*;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistrationHandler;

public class StackablePotions extends Feature
{
    public static int size;

    @Override
    public void configure()
    {
        super.configure();
        size = 16;
    }

    @Override
    public void init()
    {
        super.init();

        PotionItem potion = new PotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        SplashPotionItem splash = new SplashPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        LingeringPotionItem lingering = new LingeringPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));

        RegistrationHandler.addItemOverride("potion", potion);
        RegistrationHandler.addItemOverride("splash_potion", splash);
        RegistrationHandler.addItemOverride("lingering_potion", lingering);
    }
}
