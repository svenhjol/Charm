package svenhjol.charm.feature.wood.ebony_wood.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import svenhjol.charm.api.iface.CustomWoodMaterial;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWood;
import svenhjol.charm.charmony.feature.FeatureResolver;

import java.util.Locale;
import java.util.Optional;

public enum EbonyMaterial implements CustomWoodMaterial, FeatureResolver<EbonyWood> {
    EBONY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public BlockSetType blockSetType() {
        return feature().registers.blockSetType.get();
    }

    @Override
    public WoodType woodType() {
        return feature().registers.woodType.get();
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }

    @Override
    public Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree() {
        return Optional.of(feature().registers.treeFeature);
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return CustomWoodMaterial.super.blockProperties()
            .noOcclusion();
    }

    @Override
    public Class<EbonyWood> typeForFeature() {
        return EbonyWood.class;
    }
}
