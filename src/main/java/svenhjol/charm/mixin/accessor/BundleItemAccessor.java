package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    @Invoker
    static Stream<ItemStack> invokeGetContents(ItemStack stack) {
        throw new IllegalStateException();
    }
}
