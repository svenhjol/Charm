package svenhjol.charm.tweaks.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonItem;

public class CharmMusicDiscItem extends MusicDiscItem implements IMesonItem {
    protected String name;
    protected MesonModule module;

    public CharmMusicDiscItem(MesonModule module, String name, SoundEvent sound, Properties props, int comparatorValue) {
        super(comparatorValue, sound, props);

        this.name = name;
        this.module = module;
        register(module, name);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isEnabled() && group == ItemGroup.SEARCH) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled() {
        return module.enabled;
    }
}
