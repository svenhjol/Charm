package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.common.*;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(priority = 1, description = "Variant wood features such as barrels, chests and ladders.")
public class VariantWood extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

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

    public VariantWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    /**
     * Helper method for other mods/features to register their own custom wood variant.
     */
    public static void register(CommonFeature feature, IVariantMaterial material) {
        Resolve.feature(VariantWood.class).registers.register(feature, material);
    }
}
