package svenhjol.charm.mixin.feature.amethyst_note_block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;

import java.util.Locale;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Shadow
    public abstract boolean is(Block block);

    @Inject(
        method = "instrument",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookInstrument(CallbackInfoReturnable<NoteBlockInstrument> cir) {
        if (is(Blocks.AMETHYST_BLOCK)) {
            cir.setReturnValue(NoteBlockInstrument
                .valueOf(AmethystNoteBlock.NOTE_BLOCK_ID.toUpperCase(Locale.ROOT)));
        }
    }
}
