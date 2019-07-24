package svenhjol.meson.handler;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.iface.IMesonItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@EventBusSubscriber(bus = MOD)
public class RegistrationHandler
{
    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();
    private static HashMap<Block, Item> blockItemMap = new HashMap<>();
    private static Multimap<Class<?>, IForgeRegistryEntry<?>> entries = MultimapBuilder.hashKeys().arrayListValues().build();
//
//    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object)
//    {
//        entries.put(object.getRegistryType(), object);
//
//        if (object instanceof BlockItem) {
//            BlockItem blockItem = (BlockItem)object;
//            Block block = blockItem.getBlock();
//            blockItemMap.put(block, blockItem);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onRegister(RegistryEvent.Register event)
//    {
//        Class<?> type = event.getRegistry().getRegistrySuperType();
//
//        if (entries.containsKey(type)) {
//            Collection<IForgeRegistryEntry<?>> ourEntries = entries.get(type);
//            for (IForgeRegistryEntry<?> entry : ourEntries) {
//                event.getRegistry().register(entry);
//            }
//        }
//    }

    public static void addItem(IMesonItem item)
    {
        Item i = ((Item)item);
        ResourceLocation res = new ResourceLocation(item.getModId(), item.getBaseName());
        i.setRegistryName(res);
        ITEMS.add(i);
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    }
}
