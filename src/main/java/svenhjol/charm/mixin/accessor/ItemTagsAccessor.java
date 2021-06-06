package svenhjol.charm.mixin.accessor;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ItemTags.class)
@CharmMixin(required = true)
public interface ItemTagsAccessor {
    @Invoker()
    static Tag.Named<Item> invokeRegister(String id) {
        throw new IllegalStateException();
    }
}
