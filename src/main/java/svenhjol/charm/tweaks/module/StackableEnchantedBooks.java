package svenhjol.charm.tweaks.module;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
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
        if (isEnabled()) OverrideHandler.changeVanillaItem(item, ID);
    }
}
