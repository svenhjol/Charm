package svenhjol.charm.tweaks.feature;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.TWEAKS)
public class StackableEnchantedBooks extends Feature
{
    public static int size = 16;
    public static EnchantedBookItem item;
    public static final ResourceLocation ID = new ResourceLocation("enchanted_book");

    @Override
    public void init()
    {
        item = new EnchantedBookItem((new Item.Properties()).maxStackSize(size).rarity(Rarity.UNCOMMON));
        item.setRegistryName(ID);

        Registry.register(Registry.ITEM, ID, item); // Vanilla registry
        RegistryHandler.registerItem(item, item.getRegistryName()); // Forge registry
    }
}
