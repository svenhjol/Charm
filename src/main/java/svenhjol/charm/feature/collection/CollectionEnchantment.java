package svenhjol.charm.feature.collection;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Tags;

public class CollectionEnchantment extends Enchantment {
    public CollectionEnchantment() {
        super(Enchantment.definition(
            Tags.COLLECTION_ENCHANTABLE,
            5, // Weight (?)
            1, // Max level
            Enchantment.dynamicCost(1, 10), // Cost1 (?)
            Enchantment.dynamicCost(51, 10), // Cost2 (?)
            1, // Anvil cost
            EquipmentSlot.MAINHAND // Equipment slot
        ));
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        // Do a feature check first.
        return Globals.common(Charm.ID).isEnabled(Collection.class)
            && super.canEnchant(stack);
    }
}