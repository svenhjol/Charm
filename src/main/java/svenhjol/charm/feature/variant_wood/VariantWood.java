package svenhjol.charm.feature.variant_wood;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import svenhjol.charm.Charm;
import svenhjol.charm.api.*;
import svenhjol.charm.feature.variant_wood.registry.*;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.event.EntityUseEvent;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantChestProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Feature(mod = Charm.MOD_ID, description = "Variant wood features such as barrels, chests and ladders.")
public class VariantWood extends CharmFeature implements IRecipeRemoveProvider {
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
        // TODO: Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();
    }

    @Override
    public void register() {
        ApiHelper.consume(IVariantBarrelProvider.class,
            provider -> provider.getVariantBarrels().forEach(this::registerBarrel));

        ApiHelper.consume(IVariantBookshelfProvider.class,
            provider -> provider.getVariantBookshelves().forEach(this::registerBookshelf));

        // In future the provider could specify if there should be trapped.
        ApiHelper.consume(IVariantChestProvider.class,
            provider -> provider.getVariantChests().forEach(chest -> {
                registerChest(chest);
                registerTrappedChest(chest);
            }));

        ApiHelper.consume(IVariantChestBoatProvider.class,
            provider -> provider.getVariantChestBoatDefinitions().forEach(this::registerChestBoat));

        ApiHelper.consume(IVariantChiseledBookshelfProvider.class,
            provider -> provider.getVariantChiseledBookshelves().forEach(this::registerChiseledBookshelf));

        ApiHelper.consume(IVariantLadderProvider.class,
            provider -> provider.getVariantLadders().forEach(this::registerLadder));
    }

    @Override
    public void runWhenEnabled() {
        EntityUseEvent.INSTANCE.handle(AnimalInteraction::handle);
    }

    private void registerBarrel(IVariantMaterial material) {
        BARRELS.put(material, new CustomBarrel(getRegistry(), material));
    }

    private void registerBookshelf(IVariantMaterial material) {
        BOOKSHELVES.put(material, new CustomBookshelf(getRegistry(), material));
    }

    private void registerChest(IVariantMaterial material) {
        CHESTS.put(material, new CustomChest(getRegistry(), material));
    }

    private void registerChestBoat(IVariantChestBoatDefinition definition) {
        CHEST_BOATS.put(definition.getMaterial(), new CustomChestBoat(getRegistry(), definition));
    }

    private void registerChiseledBookshelf(IVariantMaterial material) {
        CHISELED_BOOKSHELVES.put(material, new CustomChiseledBookshelf(getRegistry(), material));
    }

    private void registerLadder(IVariantMaterial material) {
        LADDERS.put(material, new CustomLadder(getRegistry(), material));
    }

    private void registerTrappedChest(IVariantMaterial material) {
        TRAPPED_CHESTS.put(material, new CustomTrappedChest(getRegistry(), material));
    }

    private ICommonRegistry getRegistry() {
        return Charm.instance().registry();
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        var charm = Charm.instance();
        List<ResourceLocation> recipes = new ArrayList<>();

        // Remove recipes according to configuration.
        if (!variantBarrels) {
            BARRELS.keySet().forEach(material -> recipes.add(charm.makeId(material.getSerializedName() + "_barrel")));
        }
        if (!variantBookshelves) {
            BOOKSHELVES.keySet().forEach(material -> recipes.add(charm.makeId(material.getSerializedName() + "_bookshelf")));
        }
        if (!variantChests) {
            CHESTS.keySet().forEach(material -> {
                recipes.add(charm.makeId(material.getSerializedName() + "_chest"));
                recipes.add(charm.makeId(material.getSerializedName() + "_trapped_chest"));
            });
        }
        if (!variantChiseledBookshelves) {
            CHISELED_BOOKSHELVES.keySet().forEach(material -> recipes.add(charm.makeId(material.getSerializedName() + "_chiseled_bookshelf")));
        }
        if (!variantLadders) {
            LADDERS.keySet().forEach(material -> recipes.add(charm.makeId(material.getSerializedName() + "_ladder")));
        }

        return recipes;
    }
}
