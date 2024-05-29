package svenhjol.charm.feature.wood.ebony_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWood;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<EbonyWood> {
    public final Supplier<BlockSetType> blockSetType;
    public final Supplier<WoodType> woodType;
    public final Supplier<CustomWoodMaterial> material;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeFeature;
    public final ResourceKey<ConfiguredFeature<?, ?>> treesFeature;
    public final ResourceKey<PlacedFeature> treePlacement;
    public final ResourceKey<PlacedFeature> treesPlacement;

    public Registers(EbonyWood feature) {
        super(feature);
        var registry = feature().registry();

        material = () -> EbonyMaterial.EBONY;
        blockSetType = registry.blockSetType(material);
        woodType = registry.woodType(material);

        treeFeature = ResourceKey.create(Registries.CONFIGURED_FEATURE, Charm.id("ebony_tree"));
        treesFeature = ResourceKey.create(Registries.CONFIGURED_FEATURE, Charm.id("ebony_trees"));

        treePlacement = ResourceKey.create(Registries.PLACED_FEATURE, Charm.id("ebony_tree"));
        treesPlacement = ResourceKey.create(Registries.PLACED_FEATURE, Charm.id("ebony_trees"));

        registry.runnable(() -> {
            CustomWood.register(feature, new WoodDefinition());
        });
    }

    @Override
    public void onEnabled() {
        feature().registry().biomeAddition("ebony_trees",
            holder -> holder.is(Tags.GROWS_EBONY_TREES), GenerationStep.Decoration.VEGETAL_DECORATION, treesPlacement);
    }
}
