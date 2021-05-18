package svenhjol.charm.mixin.accessor;

import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(ItemTags.class)
@CharmMixin(required = true)
public interface ItemTagsAccessor {
    @Invoker()
    static Tag.Identified<Item> invokeRegister(String id) {
        throw new IllegalStateException();
    }
}
