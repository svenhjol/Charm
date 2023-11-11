package svenhjol.charm.feature.azalea_wood;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charmony_api.iface.IVariantWoodMaterial;

import java.util.Locale;

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
    public int chestBoatColor() {
        return 0x9577b1;
    }

    @Override
    public BlockSetType getBlockSetType() {
        return AzaleaWood.blockSetType.get();
    }

    @Override
    public WoodType getWoodType() {
        return AzaleaWood.woodType.get();
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return IVariantWoodMaterial.super.blockProperties()
            .noOcclusion()
            .instrument(NoteBlockInstrument.BASS);
    }
}
