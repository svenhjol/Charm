package svenhjol.charm.base.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;

public class CharmSpawnEggItem extends SpawnEggItem implements ICharmItem {
    private final CharmModule module;

    public CharmSpawnEggItem(CharmModule module, String name, EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Settings settings) {
        super(type, primaryColor, secondaryColor, settings);
        this.module = module;
        this.register(module, name);
    }

    public CharmSpawnEggItem(CharmModule module, String name, EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor) {
        this(module, name, type, primaryColor, secondaryColor, (new Item.Settings()).group(ItemGroup.MISC));
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (enabled())
            super.appendStacks(group, stacks);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
