package svenhjol.charm.feature.wood.azalea_wood.common;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.feature.FeatureResolver;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;

import java.util.Locale;

public enum Material implements CustomWoodMaterial, FeatureResolver<AzaleaWood> {
    AZALEA;

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
    public BlockBehaviour.Properties blockProperties() {
        return CustomWoodMaterial.super.blockProperties()
            .noOcclusion();
    }

    @Override
    public Class<AzaleaWood> typeForFeature() {
        return AzaleaWood.class;
    }
}
