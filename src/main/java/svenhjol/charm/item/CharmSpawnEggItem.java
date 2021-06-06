package svenhjol.charm.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import svenhjol.charm.item.ICharmItem;
import svenhjol.charm.module.CharmModule;

public class CharmSpawnEggItem extends SpawnEggItem implements ICharmItem {
    private final CharmModule module;

    public CharmSpawnEggItem(CharmModule module, String name, EntityType<? extends Mob> type, int primaryColor, int secondaryColor, Properties settings) {
        super(type, primaryColor, secondaryColor, settings);
        this.module = module;
        this.register(module, name);
    }

    public CharmSpawnEggItem(CharmModule module, String name, EntityType<? extends Mob> type, int primaryColor, int secondaryColor) {
        this(module, name, type, primaryColor, secondaryColor, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (enabled())
            super.fillItemCategory(group, stacks);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
