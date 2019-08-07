package svenhjol.charm.tweaks.feature;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistrationHandler;

public class StackableEnchantedBooks extends Feature
{
    public static int size;

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

        EnchantedBookItem item = new EnchantedBookItem((new Item.Properties()).maxStackSize(size).rarity(Rarity.UNCOMMON));

        RegistrationHandler.addItemOverride("enchanted_book", item);
    }
}
