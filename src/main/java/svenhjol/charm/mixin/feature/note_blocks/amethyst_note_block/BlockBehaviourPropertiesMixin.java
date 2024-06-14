package svenhjol.charm.mixin.feature.note_blocks.amethyst_note_block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.AmethystNoteBlock;

import java.util.Locale;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Shadow
    public abstract boolean is(Block block);

    @ModifyReturnValue(
            method = "instrument",
            at = @At("RETURN")
    )
    private NoteBlockInstrument hookInstrument(NoteBlockInstrument original) {
        if (is(Blocks.AMETHYST_BLOCK)) {
            return NoteBlockInstrument
                    .valueOf(AmethystNoteBlock.NOTE_BLOCK_ID.toUpperCase(Locale.ROOT));
        }
        return original;
    }
}
