package svenhjol.charm.api.iface;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * A custom wood material definition used when registering new item types,
 * such as chests and bookshelves for Charm's VariantWood feature.
 */
@SuppressWarnings("unused")
public interface IVariantWoodMaterial extends IVariantMaterial {
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
}
