package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.Locale;

public enum CoralMaterial implements IVariantMaterial {
    BRAIN(MapColor.COLOR_PINK),
    BUBBLE(MapColor.COLOR_PURPLE),
    FIRE(MapColor.COLOR_RED),
    HORN(MapColor.COLOR_YELLOW),
    TUBE(MapColor.COLOR_BLUE);

    private final MapColor mapColor;

    CoralMaterial(MapColor mapColor) {
        this.mapColor = mapColor;
    }

    @Override
    public MapColor mapColor() {
        return mapColor;
    }

    @Override
    public SoundType soundType() {
        return SoundType.GLASS;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
