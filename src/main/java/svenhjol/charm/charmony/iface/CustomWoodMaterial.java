package svenhjol.charm.charmony.iface;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

/**
 * A custom wood material definition used when registering new wooden item types.
 */
@SuppressWarnings("unused")
public interface CustomWoodMaterial extends CustomMaterial {
    @Override
    default NoteBlockInstrument noteBlockInstrument() {
        return NoteBlockInstrument.BASS; // Vanilla default for wood
    }

    @Override
    default boolean isFlammable() {
        return true; // Vanilla default for wood
    }

    BlockSetType blockSetType();

    WoodType woodType();

    default Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree() {
        return Optional.empty();
    }
}
