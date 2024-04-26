package svenhjol.charm.feature.amethyst_note_block;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.function.Supplier;

public class AmethystNoteBlock extends CommonFeature {
    public static final String NOTE_BLOCK_ID = "charm_amethyst";

    static Supplier<SoundEvent> sound;

    @Override
    public String description() {
        return "Place a block of amethyst under a note block to play its placement sound.";
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }
}
