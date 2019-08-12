package svenhjol.charm.tweaks.feature;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.Feature;

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
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry)
    {
        registry.register(item); // Forge registry
        Registry.register(Registry.ITEM, ID, item); // Vanilla registry
    }
}
