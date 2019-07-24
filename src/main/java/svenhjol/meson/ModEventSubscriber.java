package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.handler.RegistrationHandler;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonItem;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class ModEventSubscriber
{
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        final IForgeRegistry<Block> registry = event.getRegistry();

        // register blocks
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            registry.register(((Block)block).setRegistryName(block.getModId(), block.getBaseName()));
        }

        Meson.log("Block registration done");
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register items
        for (IMesonItem item : RegistrationHandler.ITEMS) {
            registry.register(((Item)item).setRegistryName(item.getModId(), item.getBaseName()));
        }

        // register block items
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            registry.register(block.getBlockItem().setRegistryName(block.getModId(), block.getBaseName()));
        }

        Meson.log("Item registration done");
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event)
    {
        Meson.log("Common setup done");
    }
}
