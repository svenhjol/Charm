package svenhjol.charm.feature.variant_wood;

import svenhjol.charmony.api.iface.IRecipeFilter;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;

import java.util.ArrayList;
import java.util.List;

public class VariantWoodRecipeFilters implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(

            // Barrels check.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !VariantWood.variantBarrels;
                }

                @Override
                public List<String> removes() {
                    return VariantWood.BARRELS.keySet().stream()
                        .map(material -> material.getSerializedName() + "_barrel").toList();
                }
            },

            // Bookshelves check.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !VariantWood.variantBookshelves;
                }

                @Override
                public List<String> removes() {
                    return VariantWood.BOOKSHELVES.keySet().stream()
                        .map(material -> material.getSerializedName() + "_bookshelf").toList();
                }
            },

            // Chests check.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !VariantWood.variantChests;
                }

                @Override
                public List<String> removes() {
                    List<String> out = new ArrayList<>();
                    VariantWood.CHESTS.keySet().forEach(material -> {
                        out.add(material.getSerializedName() + "_chest");
                        out.add(material.getSerializedName() + "_trapped_chest");
                    });
                    return out;
                }
            },

            // Chiseled bookshelves check.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !VariantWood.variantChiseledBookshelves;
                }

                @Override
                public List<String> removes() {
                    return VariantWood.CHISELED_BOOKSHELVES.keySet().stream()
                        .map(material -> material.getSerializedName() + "_chiseled_bookshelf").toList();
                }
            },

            // Ladders check.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !VariantWood.variantLadders;
                }

                @Override
                public List<String> removes() {
                    return VariantWood.LADDERS.keySet().stream()
                        .map(material -> material.getSerializedName() + "_ladders").toList();
                }
            }
        );
    }
}
