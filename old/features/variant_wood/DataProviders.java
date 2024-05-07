package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;

import java.util.ArrayList;
import java.util.List;

public class DataProviders implements IConditionalRecipeProvider {
    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(

            // Barrels check.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return VariantWood.variantBarrels;
                }

                @Override
                public List<String> recipes() {
                    return VariantWood.BARRELS.keySet().stream()
                        .map(material -> material.getSerializedName() + "_barrel").toList();
                }
            },

            // Bookshelves check.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return VariantWood.variantBookshelves;
                }

                @Override
                public List<String> recipes() {
                    return VariantWood.BOOKSHELVES.keySet().stream()
                        .map(material -> material.getSerializedName() + "_bookshelf").toList();
                }
            },

            // Chests check.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return VariantWood.variantChests;
                }

                @Override
                public List<String> recipes() {
                    List<String> out = new ArrayList<>();
                    VariantWood.CHESTS.keySet().forEach(material -> {
                        out.add(material.getSerializedName() + "_chest");
                        out.add(material.getSerializedName() + "_trapped_chest");
                    });
                    return out;
                }
            },

            // Chiseled bookshelves check.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return VariantWood.variantChiseledBookshelves;
                }

                @Override
                public List<String> recipes() {
                    return VariantWood.CHISELED_BOOKSHELVES.keySet().stream()
                        .map(material -> material.getSerializedName() + "_chiseled_bookshelf").toList();
                }
            },

            // Ladders check.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return VariantWood.variantLadders;
                }

                @Override
                public List<String> recipes() {
                    return VariantWood.LADDERS.keySet().stream()
                        .map(material -> material.getSerializedName() + "_ladders").toList();
                }
            }
        );
    }
}
