package svenhjol.charm.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import svenhjol.charm.loader.CharmModule;

@SuppressWarnings("unused")
public class CharmRecordItem extends RecordItem implements ICharmItem {
    protected CharmModule module;

    public CharmRecordItem(CharmModule module, String name, SoundEvent sound, int length) {
        this(module, name, sound, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), 1, length);
    }

    public CharmRecordItem(CharmModule module, String name, SoundEvent sound, Properties properties, int redstoneStrength, int length) {
        // Length isn't used in 1.18.2.
        super(redstoneStrength, sound, properties);
        this.module = module;
        this.register(module, name);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
