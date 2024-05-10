package svenhjol.charm.feature.variant_wood.common;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.ArrayList;
import java.util.List;

public final class DataProviders extends FeatureHolder<VariantWood> implements IConditionalRecipeProvider {
    public DataProviders(VariantWood feature) {
        super(feature);
    }

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
                    return feature().registers.barrels.keySet().stream()
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
                    return feature().registers.bookshelves.keySet().stream()
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
                    feature().registers.chests.keySet().forEach(material -> {
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
                    return feature().registers.chiseledBookshelves.keySet().stream()
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
                    return feature().registers.ladders.keySet().stream()
                        .map(material -> material.getSerializedName() + "_ladders").toList();
                }
            }
        );
    }
}
