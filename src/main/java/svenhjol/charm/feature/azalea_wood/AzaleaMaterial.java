package svenhjol.charm.feature.azalea_wood;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantWoodMaterial;

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
        return 0xffaf9f;
    }

    @Override
    public BlockSetType getBlockSetType() {
        return AzaleaWood.BLOCK_SET_TYPE.get();
    }

    @Override
    public WoodType getWoodType() {
        return AzaleaWood.WOOD_TYPE.get();
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }
}
