package svenhjol.charm.api.iface;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * A custom material definition used when registering new item types,
 * such as lanterns for Charm's CoralSeaLanterns feature.
 */
@SuppressWarnings("unused")
public interface IVariantMaterial extends StringRepresentable {
    default boolean isFlammable() {
        return false;
    }

    default MapColor mapColor() {
        return MapColor.COLOR_GRAY;
    }

    default int fuelTime() {
        return isFlammable() ? 300 : 0;
    }

    default int igniteChance() {
        return isFlammable() ? 30 : 0;
    }

    default int burnChance() {
        return isFlammable() ? 20 : 0;
    }

    default NoteBlockInstrument noteBlockInstrument() {
        return NoteBlockInstrument.HARP; // Vanilla default
    }

    SoundType soundType();

    default BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of()
            .sound(soundType())
            .instrument(noteBlockInstrument())
            .mapColor(mapColor());
    }
}
