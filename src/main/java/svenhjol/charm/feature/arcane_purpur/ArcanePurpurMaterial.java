package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charmony_api.iface.IVariantMaterial;

public class ArcanePurpurMaterial implements IVariantMaterial {
    @Override
    public boolean isFlammable() {
        return false;
    }

    @Override
    public SoundType soundType() {
        return SoundType.STONE;
    }

    @Override
    public MapColor mapColor() {
        return MapColor.COLOR_PURPLE;
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.copy(Blocks.PURPUR_BLOCK)
            .mapColor(mapColor())
            .sound(soundType());
    }

    @Override
    public String getSerializedName() {
        return "arcane_purpur";
    }
}
