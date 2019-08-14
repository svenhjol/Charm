package svenhjol.charm.tweaks.feature;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistryHandler;

public class StackableEnchantedBooks extends Feature
{
    public static int size;
    public static EnchantedBookItem item;
    public static final ResourceLocation ID = new ResourceLocation("enchanted_book");

    @Override
    public void configure()
    {
        super.configure();
        size = 16;
    }

    @Override
    public void init()
    {
        super.init();
        item = new EnchantedBookItem((new Item.Properties()).maxStackSize(size).rarity(Rarity.UNCOMMON));
        item.setRegistryName(ID);

        Registry.register(Registry.ITEM, ID, item); // Vanilla registry
        RegistryHandler.registerItem(item, item.getRegistryName()); // Forge registry
    }
}
