package svenhjol.charm.feature.arcane_purpur.common;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charm.charmony.iface.CustomMaterial;

import java.util.Locale;

public enum Material implements CustomMaterial {
    ARCANE_PURPUR(SoundType.STONE),
    ARCANE_PURPUR_GLYPH(SoundType.STONE),
    CHISELED_ARCANE_PURPUR(SoundType.STONE),
    CHISELED_ARCANE_PURPUR_GLYPH(SoundType.STONE);

    final SoundType soundType;

    Material(SoundType soundType) {
        this.soundType = soundType;
    }

    @Override
    public SoundType soundType() {
        return soundType;
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
        return name().toLowerCase(Locale.ROOT);
    }
}
