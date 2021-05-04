package svenhjol.charm.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.base.handler.AdvancementHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(AdvancementManager.class)
public class AdvancementManagerMixin {
    @Inject(
        method = "load",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookLoad(Map<Identifier, Advancement.Task> map, CallbackInfo ci, Map<Identifier, Advancement.Task> map2) {
        AdvancementHandler.modulesToRemove.forEach(mod -> {
            List<Identifier> keys = new ArrayList<>(map2.keySet());

            keys.stream()
                .filter(a -> a.getNamespace().equals(mod.getNamespace()))
                .filter(a -> a.getPath().startsWith(mod.getPath()))
                .forEach(map2::remove);
        });
    }
}
