package svenhjol.charm.feature.custom_wood.common;

import java.util.List;

public enum CustomType {
    BARREL,
    BOAT,
    BOOKSHELF,
    BUTTON,
    CHEST,
    CHEST_BOAT,
    CHISELED_BOOKSHELF,
    DOOR,
    FENCE,
    GATE,
    HANGING_SIGN,
    LADDER,
    LEAVES,
    LOG,
    PLANKS,
    PRESSURE_PLATE,
    SAPLING,
    SIGN,
    SLAB,
    STAIRS,
    STRIPPED_LOG,
    STRIPPED_WOOD,
    TRAPDOOR,
    TRAPPED_CHEST,
    WOOD;

    public static final List<CustomType> BUILDING_BLOCKS = List.of(
        BUTTON, DOOR, FENCE, GATE, LOG, PLANKS, PRESSURE_PLATE,
        SLAB, STAIRS, STRIPPED_LOG, STRIPPED_WOOD, TRAPDOOR, WOOD
    );
}
