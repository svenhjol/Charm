package svenhjol.charm.tools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonItem;

public class TotemOfReturningItem extends MesonItem
{
    public TotemOfReturningItem()
    {
        super("totem_of_returning", new Item.Properties()
            .group(ItemGroup.TRANSPORTATION)
            .maxStackSize(1)
        );
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
