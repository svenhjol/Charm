package svenhjol.charm.module.amethyst_note_block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "Place a block of amethyst under a note block to play its placement sound.")
public class AmethystNoteBlock extends CharmModule {
    public static SoundEvent AMETHYST;

    @Override
    public void register() {
        AMETHYST = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "amethyst"));
        try {
            var noteBlock = NoteBlockInstrument.valueOf("CHARM_AMETHYST");
            noteBlock.soundEvent = AMETHYST;
        } catch (IllegalArgumentException e) {
            // Enum value not available when mixin disabled, just skip the noteblock assignment.
        }
    }
}
