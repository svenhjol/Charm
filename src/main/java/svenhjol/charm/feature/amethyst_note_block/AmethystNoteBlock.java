package svenhjol.charm.feature.amethyst_note_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.EnumHelper;
import svenhjol.charmony.helper.PlayerHelper;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Place a block of amethyst under a note block to play its placement sound.")
public class AmethystNoteBlock extends CharmonyFeature {
    public static final String NOTE_BLOCK_ID = "charm_amethyst";
    private static Supplier<SoundEvent> AMETHYST_SOUND;

    @Override
    public void register() {
        AMETHYST_SOUND = Charm.instance().registry().soundEvent("amethyst");
    }

    /**
     * Sound registration usually happens after the custom note block enum is processed.
     * soundEvent is made accessible so we can safely set it to the registered sound here.
     */
    @Override
    public void runWhenEnabled() {
        var registry = BuiltInRegistries.SOUND_EVENT;

        registry.getResourceKey(AMETHYST_SOUND.get())
            .flatMap(registry::getHolder)
            .ifPresent(
                holder -> EnumHelper.setNoteBlockSound(
                    NOTE_BLOCK_ID,
                    holder,
                    NoteBlockInstrument.Type.BASE_BLOCK
                ));
    }

    public static void triggerPlayedAmethystNoteBlock(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 4.0D).forEach(
            player -> Advancements.trigger(Charm.instance().makeId("played_amethyst_note_block"), player)
        );
    }
}
