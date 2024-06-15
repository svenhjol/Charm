package svenhjol.charm.mixin.feature.animal_reviving;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.charmony.Resolve;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin extends Item {
    public NameTagItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getTagElement(Resolve.feature(AnimalReviving.class).handlers.getRevivableTag()) != null;
    }
}
