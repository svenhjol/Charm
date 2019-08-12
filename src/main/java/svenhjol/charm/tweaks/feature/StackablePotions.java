package svenhjol.charm.tweaks.feature;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.Feature;

public class StackablePotions extends Feature
{
    public static int size;
    public static ResourceLocation POTION_ID = new ResourceLocation("potion");
    public static ResourceLocation SPLASH_ID = new ResourceLocation("splash_potion");
    public static ResourceLocation LINGERING_ID = new ResourceLocation("lingering_potion");
    public static PotionItem potion;
    public static SplashPotionItem splash;
    public static LingeringPotionItem lingering;

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

        potion = new PotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        splash = new SplashPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));
        lingering = new LingeringPotionItem((new Item.Properties()).maxStackSize(size).group(ItemGroup.BREWING));

        potion.setRegistryName(POTION_ID);
        splash.setRegistryName(SPLASH_ID);
        lingering.setRegistryName(LINGERING_ID);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry)
    {
        registry.registerAll(potion, splash, lingering); // Forge registry

        // Vanilla registry
        Registry.register(Registry.ITEM, POTION_ID, potion);
        Registry.register(Registry.ITEM, SPLASH_ID, splash);
        Registry.register(Registry.ITEM, LINGERING_ID, lingering);
    }
}
