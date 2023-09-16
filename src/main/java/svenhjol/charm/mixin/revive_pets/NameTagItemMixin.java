package svenhjol.charm.mixin.revive_pets;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.revive_pets.RevivePets;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin extends Item {
    public NameTagItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getTagElement(RevivePets.REVIVABLE_TAG) != null;
    }
}
