package svenhjol.charm.misc.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.IMesonItem;

public class CharmItemRecord extends ItemRecord implements IMesonItem
{
    public CharmItemRecord(String modId, String name, SoundEvent sound)
    {
        super(modId + ":" + name, sound);
        register("record_" + name);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
