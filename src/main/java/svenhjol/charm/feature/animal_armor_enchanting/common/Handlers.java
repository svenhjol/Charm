package svenhjol.charm.feature.animal_armor_enchanting.common;

import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;

public final class Handlers extends FeatureHolder<AnimalArmorEnchanting> {
    public Handlers(AnimalArmorEnchanting feature) {
        super(feature);
    }

    /**
     * Called by mixin.
     * Check if the itemstack can be enchanted with the given enchantment.
     * The itemstack should always be checked to see if it's horse armor.
     */
    public boolean isValidHorseEnchantment(ItemStack stack, Enchantment enchantment) {
        return stack.getItem() instanceof HorseArmorItem
            && feature().registers.horseArmorEnchantments.contains(enchantment);
    }
}
