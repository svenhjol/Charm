package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Define all the wood items implemented by a custom wood type.
 * Used by for example Charm AzaleaWood.
 */
@SuppressWarnings("unused")
public interface CustomWoodDefinition {
    CustomWoodMaterial material();

    List<CustomType> types();

    /**
     * Override this map with items that the custom wood should appear after in the creative menu.
     */
    default Map<CustomType, Supplier<ItemLike>> creativeMenuPosition() {
        Map<CustomType, Supplier<ItemLike>> map = new LinkedHashMap<>();
        map.put(CustomType.BARREL, () -> Items.BARREL);
        map.put(CustomType.CHEST_BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(CustomType.BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(CustomType.BOOKSHELF, () -> Items.BOOKSHELF);
        map.put(CustomType.BUTTON, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.CHEST, () -> Items.CHEST);
        map.put(CustomType.CHISELED_BOOKSHELF,   () -> Items.CHISELED_BOOKSHELF);
        map.put(CustomType.DOOR, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.FENCE, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.GATE, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.HANGING_SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(CustomType.LADDER, () -> Items.LADDER);
        map.put(CustomType.LEAVES, () -> Items.ACACIA_LEAVES);
        map.put(CustomType.LOG, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.PLANKS, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.PRESSURE_PLATE, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.SAPLING, () -> Items.ACACIA_SAPLING);
        map.put(CustomType.SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(CustomType.SLAB, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.STAIRS, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.STRIPPED_LOG, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.STRIPPED_WOOD, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.TRAPDOOR, () -> Items.ACACIA_BUTTON);
        map.put(CustomType.TRAPPED_CHEST, () -> Items.TRAPPED_CHEST);
        map.put(CustomType.WOOD, () -> Items.ACACIA_BUTTON);
        return map;
    }
}
