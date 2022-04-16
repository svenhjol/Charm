package svenhjol.charm.mixin.weathering_iron;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.init.CharmTags;

/**
 * Waxed iron should make the same sound as a normal iron block.
 */
@Mixin(NoteBlockInstrument.class)
public class WaxedIronNoteBlockMixin {

    @Inject(
        method = "byState",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookByState(BlockState state, CallbackInfoReturnable<NoteBlockInstrument> cir) {
        if (state.is(CharmTags.IRON)) {
            cir.setReturnValue(NoteBlockInstrument.IRON_XYLOPHONE);
        }
    }
}
