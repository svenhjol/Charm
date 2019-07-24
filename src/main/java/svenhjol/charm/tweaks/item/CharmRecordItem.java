package svenhjol.charm.tweaks.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.handler.RegistrationHandler;
import svenhjol.meson.iface.IMesonItem;

public class CharmRecordItem extends MusicDiscItem implements IMesonItem
{
    protected String baseName;

    public CharmRecordItem(String baseName, SoundEvent sound, int comparatorValue)
    {
        super(comparatorValue, sound, new Properties().maxStackSize(1).group(ItemGroup.MISC).rarity(Rarity.RARE));
        this.baseName = baseName;
        RegistrationHandler.addItem(this);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }
}
