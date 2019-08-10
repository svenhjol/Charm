package svenhjol.meson.handler;

import net.minecraft.block.Block;
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
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterBlocks(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterItems(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterEffects(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterPotions(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterTileEntities(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterContainers(final RegistryEvent.Register<ContainerType<?>> event)
    {
        MesonLoader.forEachEnabledFeature(f -> f.onRegisterContainers(event.getRegistry()));
    }

    @SubscribeEvent
    public static void onRegisterSounds(final RegistryEvent.Register<SoundEvent> event)
    {
        SOUNDS.forEach(sound -> event.getRegistry().register(sound));
    }
}
