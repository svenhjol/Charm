package svenhjol.charm.mixin.feature.revive_pets;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.foundation.Resolve;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin extends Item {
    public NameTagItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(Resolve.feature(RevivePets.class).registers.data());
    }
}
