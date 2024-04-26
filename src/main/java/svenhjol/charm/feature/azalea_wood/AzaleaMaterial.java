package svenhjol.charm.feature.azalea_wood;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.Locale;

@SuppressWarnings("RedundantMethodOverride")
public enum AzaleaMaterial implements IVariantWoodMaterial {
    AZALEA;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public boolean isFlammable() {
        return true;
    }

    @Override
    public BlockSetType blockSetType() {
        return AzaleaWood.blockSetType.get();
    }

    @Override
    public WoodType woodType() {
        return AzaleaWood.woodType.get();
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return IVariantWoodMaterial.super.blockProperties()
            .noOcclusion();
    }
}
