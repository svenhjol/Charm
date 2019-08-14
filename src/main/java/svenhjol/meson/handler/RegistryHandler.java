package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import svenhjol.meson.MesonLoader;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class RegistryHandler
{
    public static List<SoundEvent> SOUNDS = new ArrayList<>();

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerBlocks(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterContainers(final RegistryEvent.Register<ContainerType<?>> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerContainers(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerItems(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerEffects(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerEnchantments(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerPotions(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerTileEntities(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterSounds(final RegistryEvent.Register<SoundEvent> event)
    {
        MesonLoader.allEnabledFeatures(f -> f.registerSounds(event.getRegistry()));
    }
}
