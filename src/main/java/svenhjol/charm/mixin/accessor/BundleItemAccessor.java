package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    /**
     * Allows matching of the items tag when sorting items in bundles.
     */
    @Accessor("TAG_ITEMS")
    static String getTagItems() {
        throw new AssertionError();
    }

    /**
     * Allows fetching of a bundle's contents when sorting items.
     */
    @Invoker("getContents")
    static Stream<ItemStack> invokeGetContents(ItemStack itemStack) {
        throw new AssertionError();
    }
}
