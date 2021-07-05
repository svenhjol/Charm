package svenhjol.charm.mixin.helper;

import net.minecraft.advancements.AdvancementList;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AdvancementList.class)
public class ChangeAdvancementErrorTypeMixin {
    /**
     * Make the advancement error less angry.
     */
    @Redirect(
        method = "add",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            remap = false
        )
    )
    private void hookLogError(Logger logger, String message, Object key, Object val) {
        logger.info(message, key, val);
    }
}
