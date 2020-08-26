package svenhjol.meson.helper;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
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
    public static Item changeItem(Identifier id, Item item) {
        int rawId = Registry.ITEM.getRawId(Registry.ITEM.get(id));
        return (Item)((MutableRegistry)Registry.ITEM).set(rawId, RegistryKey.of(Registry.ITEM.getKey(), id), item, Lifecycle.stable());
    }

    public static Block changeBlock(Identifier id, Block block) {
        int rawId = Registry.BLOCK.getRawId(Registry.BLOCK.get(id));
        return (Block)((MutableRegistry)Registry.BLOCK).set(rawId, RegistryKey.of(Registry.BLOCK.getKey(), id), block, Lifecycle.stable());
    }

    public static void changeDispenserBehavior(Item existingItem, Item newItem) {
        DispenserBehavior splashBehavior = DispenserBlockAccessor.getDispenseBehaviorRegistry().get(existingItem);
        DispenserBlock.registerBehavior(newItem, splashBehavior);
    }
}
