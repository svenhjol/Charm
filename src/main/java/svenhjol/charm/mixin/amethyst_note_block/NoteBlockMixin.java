package svenhjol.charm.mixin.amethyst_note_block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Shadow @Final public static EnumProperty<NoteBlockInstrument> INSTRUMENT;

    /**
     * This injection triggers the amethyst advancement.
     */
    @Inject(
        method = "playNote",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;)V"
        )
    )
    private void hookPlayNote(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        var instrument = state.getValue(INSTRUMENT);
        var name = instrument.getSerializedName();
        if (name.equals(AmethystNoteBlock.NOTE_BLOCK_ID)
            && level instanceof ServerLevel serverLevel) {
            AmethystNoteBlock.triggerPlayAmethystNoteBlock(serverLevel, pos);
        }
    }
}
