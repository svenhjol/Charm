package svenhjol.meson;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IMesonItem
{
    String getModId();

    default void register(String name)
    {
        // set the name of the item
        Item item = (Item)this;
        item.setTranslationKey(getModId() + ":" + name);
        item.setRegistryName(getModId() + ":" + name);

        ProxyRegistry.items.add(item); // so we can register models
        ProxyRegistry.register(item);
    }

    interface IItemCustomModelRegister
    {
        void registerItemModels(Item item);
    }

    interface IVariants
    {
        List<ResourceLocation> getVariants();
    }
}
