package svenhjol.charm.base.helper;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import svenhjol.charm.mixin.accessor.BlockAccessor;
import svenhjol.charm.mixin.accessor.DispenserBlockAccessor;
import svenhjol.charm.mixin.accessor.ItemAccessor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class OverrideHandler {
    private static final Map<Item, String> defaultItemKeys = new HashMap<>();
    private static final Map<Block, String> defaultBlockKeys = new HashMap<>();

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

    public static void changeItemTranslationKey(Item item, String newKey) {
        if (!defaultItemKeys.containsKey(item)) {
            // record the default before trying to set it
            defaultItemKeys.put(item, item.getTranslationKey());
        }

        if (newKey == null)
            newKey = defaultItemKeys.get(item);

        ((ItemAccessor)item).setTranslationKey(newKey);
    }

    public static void changeBlockTranslationKey(Block block, String newKey) {
        if (!defaultBlockKeys.containsKey(block)) {
            // record the default before trying to set it
            defaultBlockKeys.put(block, block.getTranslationKey());
        }

        if (newKey == null)
            newKey = defaultBlockKeys.get(block);

        ((BlockAccessor)block).setTranslationKey(newKey);
    }
}
