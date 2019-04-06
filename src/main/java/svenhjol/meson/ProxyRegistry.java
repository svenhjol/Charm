package svenhjol.meson;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxyRegistry
{
    public static List<Item> items = new ArrayList<>();
    public static List<Block> blocks = new ArrayList<>();

    private static Multimap<Class<?>, IForgeRegistryEntry<?>> entries = MultimapBuilder.hashKeys().arrayListValues().build();

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object)
    {
        entries.put(object.getRegistryType(), object);
    }

    public static <T extends IForgeRegistryEntry<T>> void registerAll(IForgeRegistryEntry<T>... object)
    {
        for (IForgeRegistryEntry<T> entry : object) {
            entries.put(entry.getRegistryType(), entry);
        }
    }

    /**
     * This is called by Forge (magically) and iterates through
     * all the entries, registering them by type.
     */
    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register event)
    {
       Class<?> type = event.getRegistry().getRegistrySuperType();

       if (entries.containsKey(type)) {
           Collection<IForgeRegistryEntry<?>> ourEntries = entries.get(type);
           for (IForgeRegistryEntry<?> entry : ourEntries) {
               event.getRegistry().register(entry);
           }
       }
    }
}