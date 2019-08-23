package svenhjol.charm.tweaks.module;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS)
public class StackableEnchantedBooks extends MesonModule
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
