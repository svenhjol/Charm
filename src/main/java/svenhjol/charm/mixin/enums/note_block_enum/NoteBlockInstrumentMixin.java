package svenhjol.charm.mixin.enums.note_block_enum;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.enums.NoteBlockEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Enum solution from LudoCrypt:
 * @link <a href="https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556">...</a>
 * Each charm mod that adds enums should have a NoteBlockEnums with list of strings to inject.
 */
@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(NoteBlockInstrument.class)
@Unique
public class NoteBlockInstrumentMixin {
    @Shadow
    @Final
    @Mutable
    private static NoteBlockInstrument[] $VALUES;

    static {
        for (var noteBlockEnum : NoteBlockEnums.NOTE_BLOCK_ENUMS) {
            addVariant(noteBlockEnum.toUpperCase(), noteBlockEnum.toLowerCase(), SoundEvents.NOTE_BLOCK_HARP, NoteBlockInstrument.Type.BASE_BLOCK);
        }
    }

    @Invoker("<init>")
    public static NoteBlockInstrument invokeInit(String internalName, int internalId, String noteName, Holder<SoundEvent> holder, NoteBlockInstrument.Type type) {
        throw new AssertionError();
    }

    @Unique
    private static void addVariant(String newName, String noteName, Holder<SoundEvent> holder, NoteBlockInstrument.Type type) {
        List<NoteBlockInstrument> variants = new ArrayList<>(Arrays.asList(NoteBlockInstrumentMixin.$VALUES));
        variants.add(invokeInit(newName.toUpperCase(Locale.ROOT), variants.get(variants.size() - 1).ordinal() + 1, noteName, holder, type));
        NoteBlockInstrumentMixin.$VALUES = variants.toArray(new NoteBlockInstrument[0]);
    }
}
