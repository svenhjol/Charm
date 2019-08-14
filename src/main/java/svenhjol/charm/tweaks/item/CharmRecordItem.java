package svenhjol.charm.tweaks.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonItem;

public class CharmRecordItem extends MusicDiscItem implements IMesonItem
{
    private String baseName;

    public CharmRecordItem(String baseName, SoundEvent sound, int comparatorValue)
    {
        super(comparatorValue, sound, new Properties().maxStackSize(1).group(ItemGroup.MISC).rarity(Rarity.RARE));
        this.baseName = baseName;

        register(new ResourceLocation(Charm.MOD_ID, baseName));
    }
}
