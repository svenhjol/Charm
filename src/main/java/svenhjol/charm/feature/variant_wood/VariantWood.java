package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VariantWood extends CommonFeature {
    static final Map<IVariantMaterial, CustomBarrel> BARRELS = new LinkedHashMap<>();
    static final Map<IVariantMaterial, CustomBookshelf> BOOKSHELVES = new LinkedHashMap<>();
    static final Map<IVariantMaterial, CustomChest> CHESTS = new LinkedHashMap<>();
    static final Map<IVariantMaterial, CustomChiseledBookshelf> CHISELED_BOOKSHELVES = new LinkedHashMap<>();
    static final Map<IVariantMaterial, CustomLadder> LADDERS = new LinkedHashMap<>();
    static final Map<IVariantMaterial, CustomTrappedChest> TRAPPED_CHESTS = new LinkedHashMap<>();

    @Configurable(name = "Variant barrels", description = "If true, enables barrels made from different kinds of wood.")
    public static boolean variantBarrels = true;

    @Configurable(name = "Variant bookshelves", description = "If true, enables bookshelves made from different kinds of wood.")
    public static boolean variantBookshelves = true;

    @Configurable(name = "Variant chests", description = "If true, enables chests made from different kinds of wood.")
    public static boolean variantChests = true;

    @Configurable(name = "Variant chiseled bookshelves", description = "If true, enables chiseled bookshelves made from different kinds of wood.")
    public static boolean variantChiseledBookshelves = true;

    @Configurable(name = "Variant ladders", description = "If true, enables ladders made from different kinds of wood.")
    public static boolean variantLadders = true;

    @Override
    public String description() {
        return "Variant wood features such as barrels, chests and ladders.";
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(
            new CommonEnumsRegister(this), // register the enums early!
            new CommonBlocksRegister(this),
            new CommonEventsRegister(this)
        );
    }

    /**
     * API method for other mods/features to register their own custom wood variant.
     */
    public static void registerWood(CommonRegistry registry, IVariantMaterial material) {
        BARRELS.put(material, new CustomBarrel(registry, material));
        BOOKSHELVES.put(material, new CustomBookshelf(registry, material));
        CHESTS.put(material, new CustomChest(registry, material));
        CHISELED_BOOKSHELVES.put(material, new CustomChiseledBookshelf(registry, material));
        LADDERS.put(material, new CustomLadder(registry, material));
        TRAPPED_CHESTS.put(material, new CustomTrappedChest(registry, material));
    }
}
