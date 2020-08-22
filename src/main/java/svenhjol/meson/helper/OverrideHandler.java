package svenhjol.meson.helper;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import svenhjol.charm.mixin.accessor.DispenserBlockAccessor;

@SuppressWarnings({"unchecked", "rawtypes"})
public class OverrideHandler {
    public static void overrideVanillaItem(Identifier id, Item item) {
        int rawId = Registry.ITEM.getRawId(Registry.ITEM.get(id));
        ((MutableRegistry)Registry.ITEM).set(rawId, RegistryKey.of(Registry.ITEM.getKey(), id), item, Lifecycle.stable());
    }

    public static void overrideDispenserBehavior(Item existingItem, Item newItem) {
        DispenserBehavior splashBehavior = DispenserBlockAccessor.getDispenseBehaviorRegistry().get(existingItem);
        DispenserBlock.registerBehavior(newItem, splashBehavior);
    }
}
