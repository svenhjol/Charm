package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class RegistryHandler
{
    public static Map<Class<?>, List<IForgeRegistryEntry<?>>> objects = new HashMap();

    public static void registerBlock(Block block, ResourceLocation res)
    {
        addRegisterable(block, res);

        // also register the block item
        if (block instanceof IMesonBlock) {
            addRegisterable(((IMesonBlock)block).getBlockItem(), res);
        }
    }

    public static void registerContainer(ContainerType<?> container)
    {
        addRegisterable(container, container.getRegistryName());
    }

    public static void registerEffect(Effect effect, ResourceLocation res)
    {
        addRegisterable(effect, res);
    }

    public static void registerEnchantment(Enchantment enchantment, ResourceLocation res)
    {
        addRegisterable(enchantment, res);
    }

    public static void registerItem(Item item, ResourceLocation res)
    {
        addRegisterable(item, res);
    }

    public static void registerPotion(Potion potion, ResourceLocation res)
    {
        addRegisterable(potion, res);

        if (potion instanceof IMesonPotion) {
            ((IMesonPotion)potion).registerRecipe();
        }
    }

    public static void registerSound(SoundEvent sound)
    {
        addRegisterable(sound, sound.getRegistryName());
    }

    public static void registerTile(TileEntityType<?> tile)
    {
        addRegisterable(tile, tile.getRegistryName());
    }

    private static void addRegisterable(IForgeRegistryEntry<?> obj, ResourceLocation res)
    {
        Class<?> type = obj.getRegistryType();
        if (!objects.containsKey(type)) objects.put(type, new ArrayList<>());

        if (res == null) {
            Meson.warn("ResourceLocation must not be null", obj);
        } else if (objects.containsKey(type) && objects.get(type).contains(obj)) {
            Meson.warn("Attempted to add an object to the registry that already exists", obj);
        } else {
            if (obj.getRegistryName() != null) {
                Meson.warn("Attempted to set registry name with existing registry name", obj);
            } else {
                obj.setRegistryName(res);
            }
            objects.get(type).add(obj);
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegistryEvent.Register event)
    {
        IForgeRegistry registry = event.getRegistry();
        Class<?> type = registry.getRegistrySuperType();

        if (objects.containsKey(type)) {
            objects.get(type).forEach(registry::register);
            objects.remove(type);
        }
    }
}
