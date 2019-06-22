package svenhjol.meson.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Adapted from AutoRegLib ProxyRegistry.
 * @link {https://github.com/Vazkii/AutoRegLib/blob/master/src/main/java/vazkii/arl/util/ProxyRegistry.java}
 */
public class ProxyRegistry
{
    public static List<Item> items = new ArrayList<>();
    public static List<Block> blocks = new ArrayList<>();
    private static HashMap<Block, Item> itemBlockMap = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    private static Multimap<Class<?>, IForgeRegistryEntry<?>> entries = MultimapBuilder.hashKeys().arrayListValues().build();

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object)
    {
        entries.put(object.getRegistryType(), object);

        if (object instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) object;
            Block block = itemBlock.getBlock();
            itemBlockMap.put(block, itemBlock);
        }
    }

    @SafeVarargs
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
    @SuppressWarnings("unchecked")
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

    public static Item getItemMapping(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        if (item == Items.AIR && itemBlockMap.containsKey(block)) {
            return itemBlockMap.get(block);
        }
        return item;
    }

    public static ItemStack newStack(Block block)
    {
        return newStack(block, 1);
    }

    public static ItemStack newStack(Block block, int size)
    {
        return newStack(block, size, 0);
    }

    public static ItemStack newStack(Block block, int size, int meta)
    {
        return newStack(getItemMapping(block), size, meta);
    }

    public static ItemStack newStack(Item item)
    {
        return newStack(item, 1);
    }

    public static ItemStack newStack(Item item, int size)
    {
        return newStack(item, size, 0);
    }

    public static ItemStack newStack(Item item, int size, int meta)
    {
        return new ItemStack(item, size, meta);
    }
}