package svenhjol.charm.feature.amethyst_note_block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.helper.EnumHelper;

public final class CommonRegistration extends Register<AmethystNoteBlock> {
    public CommonRegistration(AmethystNoteBlock feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        AmethystNoteBlock.sound = feature.registry().soundEvent("amethyst");
    }

    /**
     * Sound registration usually happens after the custom note block enum is processed.
     * soundEvent is made accessible so we can safely set it to the registered sound here.
     */
    @Override
    public void onEnabled() {
        var registry = BuiltInRegistries.SOUND_EVENT;

        registry.getResourceKey(AmethystNoteBlock.sound.get())
            .flatMap(registry::getHolder)
            .ifPresent(
                holder -> EnumHelper.setNoteBlockSound(
                    AmethystNoteBlock.NOTE_BLOCK_ID,
                    holder,
                    NoteBlockInstrument.Type.BASE_BLOCK
                ));
    }
}
