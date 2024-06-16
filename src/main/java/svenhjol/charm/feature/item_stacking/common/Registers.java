package svenhjol.charm.feature.item_stacking.common;

import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.common.helper.ItemOverrideHelper;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_stacking.ItemStacking;

public final class Registers extends RegisterHolder<ItemStacking> {
    public Registers(ItemStacking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ItemOverrideHelper.maxStackSize(Items.ENCHANTED_BOOK, feature().enchantedBookStackSize());
        ItemOverrideHelper.maxStackSize(Items.POTION, feature().potionStackSize());
        ItemOverrideHelper.maxStackSize(Items.SPLASH_POTION, feature().splashPotionStackSize());
        ItemOverrideHelper.maxStackSize(Items.LINGERING_POTION, feature().lingeringPotionStackSize());
        ItemOverrideHelper.maxStackSize(Items.BEETROOT_SOUP, feature().stewStackSize());
        ItemOverrideHelper.maxStackSize(Items.MUSHROOM_STEW, feature().stewStackSize());
        ItemOverrideHelper.maxStackSize(Items.RABBIT_STEW, feature().stewStackSize());
        ItemOverrideHelper.maxStackSize(Items.SUSPICIOUS_STEW, feature().suspiciousStewStackSize());
    }
}
