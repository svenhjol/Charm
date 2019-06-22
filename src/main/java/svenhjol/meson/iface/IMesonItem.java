package svenhjol.meson.iface;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.meson.registry.ProxyRegistry;

import java.util.List;

public interface IMesonItem
{
    default void register(String name)
    {
        Item item = (Item)this;

        // set the name of the item
        ResourceLocation res = new ResourceLocation(getModId() + ":" + name);
        item.setTranslationKey(res.toString());
        item.setRegistryName(res);

        // register the item
        ProxyRegistry.register(item);

        // for model handler etc
        ProxyRegistry.items.add(item);
    }

    String getModId();

    interface IItemCustomModel
    {
        void registerModels(Item item);
    }

    interface IItemVariants
    {
        List<ResourceLocation> getVariants();
    }

    @SuppressWarnings("unused")
    interface IItemColorHandler
    {
        @SideOnly(Side.CLIENT)
        IItemColor getItemColor();
    }
}
