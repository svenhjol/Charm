package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.entity.vehicle.Boat;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.registry.*;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.EntityUseEvent;
import svenhjol.charmony_api.iface.IVariantChestBoatDefinition;
import svenhjol.charmony_api.iface.IVariantMaterial;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.HashMap;
import java.util.Map;

@Feature(mod = Charm.MOD_ID, description = "Variant wood features such as barrels, chests and ladders.")
public class VariantWood extends CharmonyFeature {
    static final Map<IVariantMaterial, CustomBarrel> BARRELS = new HashMap<>();
    static final Map<IVariantMaterial, CustomBookshelf> BOOKSHELVES = new HashMap<>();
    static final Map<IVariantMaterial, CustomChest> CHESTS = new HashMap<>();
    static final Map<IVariantMaterial, CustomChestBoat> CHEST_BOATS = new HashMap<>();
    static final Map<IVariantMaterial, CustomChiseledBookshelf> CHISELED_BOOKSHELVES = new HashMap<>();
    static final Map<IVariantMaterial, CustomLadder> LADDERS = new HashMap<>();
    static final Map<IVariantMaterial, CustomTrappedChest> TRAPPED_CHESTS = new HashMap<>();

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

    @SuppressWarnings("unused")
    @Override
    public void preRegister() {
        // Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();
    }

    @Override
    public void register() {
        new VariantWoodApiConsumers(this);

        // Register recipe removal.
        CharmonyApi.registerProvider(new VariantWoodRecipeFilters());
    }

    @Override
    public void runWhenEnabled() {
        EntityUseEvent.INSTANCE.handle(AnimalInteraction::handle);
    }

    void registerBarrel(IVariantMaterial material) {
        BARRELS.put(material, new CustomBarrel(getRegistry(), material));
    }

    void registerBookshelf(IVariantMaterial material) {
        BOOKSHELVES.put(material, new CustomBookshelf(getRegistry(), material));
    }

    void registerChest(IVariantMaterial material) {
        CHESTS.put(material, new CustomChest(getRegistry(), material));
    }

    void registerChestBoat(IVariantChestBoatDefinition definition) {
        CHEST_BOATS.put(definition.getMaterial(), new CustomChestBoat(getRegistry(), definition));
    }

    void registerChiseledBookshelf(IVariantMaterial material) {
        CHISELED_BOOKSHELVES.put(material, new CustomChiseledBookshelf(getRegistry(), material));
    }

    void registerLadder(IVariantMaterial material) {
        LADDERS.put(material, new CustomLadder(getRegistry(), material));
    }

    void registerTrappedChest(IVariantMaterial material) {
        TRAPPED_CHESTS.put(material, new CustomTrappedChest(getRegistry(), material));
    }

    private ICommonRegistry getRegistry() {
        return Charm.instance().registry();
    }
}
