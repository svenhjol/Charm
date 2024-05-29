package svenhjol.charm.feature.item_stacking.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.item_stacking.ItemStacking;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.charmony.common.helper.ItemOverrideHelper;

public final class Registers extends RegisterHolder<ItemStacking> {
    public Registers(ItemStacking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ItemOverrideHelper.dataComponentValue(Items.ENCHANTED_BOOK,
            DataComponents.MAX_STACK_SIZE, feature().enchantedBookStackSize());
        ItemOverrideHelper.dataComponentValue(Items.POTION,
            DataComponents.MAX_STACK_SIZE, feature().potionStackSize());
        ItemOverrideHelper.dataComponentValue(Items.SPLASH_POTION,
            DataComponents.MAX_STACK_SIZE, feature().splashPotionStackSize());
        ItemOverrideHelper.dataComponentValue(Items.LINGERING_POTION,
            DataComponents.MAX_STACK_SIZE, feature().lingeringPotionStackSize());
        ItemOverrideHelper.dataComponentValue(Items.BEETROOT_SOUP,
            DataComponents.MAX_STACK_SIZE, feature().stewStackSize());
        ItemOverrideHelper.dataComponentValue(Items.MUSHROOM_STEW,
            DataComponents.MAX_STACK_SIZE, feature().stewStackSize());
        ItemOverrideHelper.dataComponentValue(Items.RABBIT_STEW,
            DataComponents.MAX_STACK_SIZE, feature().stewStackSize());
        ItemOverrideHelper.dataComponentValue(Items.SUSPICIOUS_STEW,
            DataComponents.MAX_STACK_SIZE, feature().suspiciousStewStackSize());
    }
}
