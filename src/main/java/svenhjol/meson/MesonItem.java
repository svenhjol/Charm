package svenhjol.meson;

import net.minecraft.item.Item;
import svenhjol.meson.handler.RegistrationHandler;
import svenhjol.meson.iface.IMesonItem;

public abstract class MesonItem extends Item implements IMesonItem
{
    protected String baseName;

    public MesonItem(String baseName, Item.Properties props)
    {
        super(props);
        this.baseName = baseName;
        RegistrationHandler.addItem(this);
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }
}
