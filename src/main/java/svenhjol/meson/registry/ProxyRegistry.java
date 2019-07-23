package svenhjol.meson.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.charm.Charm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = Charm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProxyRegistry
{
    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();
    private static HashMap<Block, Item> blockItemMap = new HashMap<>();
    private static Multimap<Class<?>, IForgeRegistryEntry<?>> entries = MultimapBuilder.hashKeys().arrayListValues().build();

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object)
    {
        entries.put(object.getRegistryType(), object);

        if (object instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)object;
            Block block = blockItem.getBlock();
            blockItemMap.put(block, blockItem);
        }
    }

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
