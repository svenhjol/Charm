package svenhjol.charm.feature.azalea_wood.common;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;

import java.util.Locale;

@SuppressWarnings("RedundantMethodOverride")
public enum Material implements IVariantWoodMaterial {
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
        return AzaleaWood.registers.blockSetType.get();
    }

    @Override
    public WoodType woodType() {
        return AzaleaWood.registers.woodType.get();
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
