package svenhjol.charm.feature.casks.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<Casks> {
    public Handlers(Casks feature) {
        super(feature);
    }

    public ItemStack getFilledWaterBottle() {
        return PotionContents.createItemStack(Items.POTION, Potions.WATER);
    }

    public boolean isValidPotion(ItemStack potion) {
        boolean valid = potion.is(Items.POTION);

        if (!valid && feature().allowSplashAndLingering()) {
            valid = potion.is(Items.LINGERING_POTION) || potion.is(Items.SPLASH_POTION);
        }

        return valid;
    }

    public PotionContents getPotion(ItemStack stack) {
        if (isValidPotion(stack)) {
            return stack.get(DataComponents.POTION_CONTENTS);
        }
        return PotionContents.EMPTY;
    }

    public ItemStack makeCustomPotion(List<MobEffectInstance> effects) {
        var stack = feature().handlers.getFilledWaterBottle();
        var basePotion = getPotion(stack);

        var newPotionContents = new PotionContents(basePotion.potion(), basePotion.customColor(), effects);
        stack.set(DataComponents.POTION_CONTENTS, newPotionContents);

        var customName = Component.translatable("item.charm.home_brew");
        stack.set(DataComponents.CUSTOM_NAME, customName);

        return stack;
    }

    public void hopperAddToCask(CaskBlockEntity cask) {
        var input = cask.items.get(0);
        var output = cask.items.get(1);

        if (input.is(Items.GLASS_BOTTLE) && output.isEmpty()) {
            var out = cask.take();
            if (out != null) {
                cask.items.set(1, out);
                cask.setChanged();
            } else {
                cask.items.set(1, new ItemStack(Items.GLASS_BOTTLE));
            }
            input.shrink(1);
        } else if (isValidPotion(input) && output.isEmpty()) {
            var result = cask.add(input);
            if (result) {
                cask.items.set(1, new ItemStack(Items.GLASS_BOTTLE));
                cask.setChanged();
            } else {
                cask.items.set(1, input);
            }
            input.shrink(1);
        }
    }

}
