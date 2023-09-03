package svenhjol.charm.mixin.beekeepers;

import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Set beehive point of interest types to have a ticket of 1 instead of 0.
 * This allows a villager to use it as a job site.
 */
@Mixin(PoiTypes.class)
public class PoiTypesMixin {
    @ModifyArg(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/village/poi/PoiTypes;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Ljava/util/Set;II)Lnet/minecraft/world/entity/ai/village/poi/PoiType;",
            ordinal = 15
        ),
        index = 3
    )
    private static int hookAddTicket(int original) {
        return 1;
    }
}
