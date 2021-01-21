package svenhjol.charm.mixin.accessor;

import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemTags.class)
public interface ItemTagsAccessor {
    @Invoker()
    static Tag.Identified<Item> invokeRegister(String id) {
        throw new IllegalStateException();
    }
}
