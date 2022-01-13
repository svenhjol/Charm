package svenhjol.charm.mixin.amethyst_note_block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.amethyst_note_block.AmethystNoteBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(NoteBlockInstrument.class)
@Unique
public class AddNoteBlockInstrumentMixin {
    @Shadow
    @Final
    @Mutable
    private static NoteBlockInstrument[] $VALUES;

    @Mutable
    @Shadow @Final private SoundEvent soundEvent;

    static {
        addVariant("CHARM_AMETHYST", "charm_amethyst", AmethystNoteBlock.AMETHYST);
    }

    @Invoker("<init>")
    public static NoteBlockInstrument invokeInit(String internalName, int internalId, String noteName, SoundEvent sound) {
        throw new AssertionError();
    }

    @Inject(
        method = "byState",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookByState(BlockState state, CallbackInfoReturnable<NoteBlockInstrument> cir) {
        if (state.is(Blocks.AMETHYST_BLOCK)) {
            cir.setReturnValue(NoteBlockInstrument.valueOf("CHARM_AMETHYST"));
        }
    }

    private static void addVariant(String newName, String noteName, SoundEvent sound) {
        List<NoteBlockInstrument> variants = new ArrayList<>(Arrays.asList(AddNoteBlockInstrumentMixin.$VALUES));
        variants.add(invokeInit(newName, variants.get(variants.size() - 1).ordinal() + 1, noteName, sound));
        AddNoteBlockInstrumentMixin.$VALUES = variants.toArray(new NoteBlockInstrument[0]);
    }
}
