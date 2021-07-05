package svenhjol.charm.helper;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import svenhjol.charm.mixin.accessor.BlockAccessor;
import svenhjol.charm.mixin.accessor.DispenserBlockAccessor;
import svenhjol.charm.mixin.accessor.ItemAccessor;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"unused", "UnusedReturnValue", "unchecked", "rawtypes"})
public class OverrideHandler {
    private static final Map<Item, String> defaultItemKeys = new HashMap<>();
    private static final Map<Block, String> defaultBlockKeys = new HashMap<>();

    public static Item changeItem(ResourceLocation id, Item item) {
        int rawId = Registry.ITEM.getId(Registry.ITEM.get(id));
        return (Item)((WritableRegistry)Registry.ITEM).registerMapping(rawId, ResourceKey.create(Registry.ITEM.key(), id), item, Lifecycle.stable());
    }

    public static Block changeBlock(ResourceLocation id, Block block) {
        int rawId = Registry.BLOCK.getId(Registry.BLOCK.get(id));
        return (Block)((WritableRegistry)Registry.BLOCK).registerMapping(rawId, ResourceKey.create(Registry.BLOCK.key(), id), block, Lifecycle.stable());
    }

    public static void changeDispenserBehavior(Item existingItem, Item newItem) {
        DispenseItemBehavior splashBehavior = DispenserBlockAccessor.getDispenserRegistry().get(existingItem);
        DispenserBlock.registerBehavior(newItem, splashBehavior);
    }

    public static void changeItemTranslationKey(Item item, String newKey) {
        if (!defaultItemKeys.containsKey(item)) {
            // record the default before trying to set it
            defaultItemKeys.put(item, item.getDescriptionId());
        }

        if (newKey == null)
            newKey = defaultItemKeys.get(item);

        ((ItemAccessor)item).setDescriptionId(newKey);
    }

    public static void changeBlockTranslationKey(Block block, String newKey) {
        if (!defaultBlockKeys.containsKey(block)) {
            // record the default before trying to set it
            defaultBlockKeys.put(block, block.getDescriptionId());
        }

        if (newKey == null)
            newKey = defaultBlockKeys.get(block);

        ((BlockAccessor)block).setDescriptionId(newKey);
    }
}
