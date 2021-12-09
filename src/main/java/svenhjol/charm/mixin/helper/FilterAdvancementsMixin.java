package svenhjol.charm.mixin.helper;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.AdvancementHelper;

import java.util.Map;

@Mixin(AdvancementList.class)
public class FilterAdvancementsMixin {
    /**
     * Conditionally remove advancements from the map if their
     * corresponding Charm module is disabled.
     *
     * Disabling this mixin will cause all advancements to be
     * loaded and if any Charm modules are disabled then
     * some advancements may not be completable.
     */
    @Inject(
        method = "add",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;",
            shift = At.Shift.AFTER,
            remap = false
        )
    )
    private void hookAdd(Map<ResourceLocation, Advancement.Builder> map, CallbackInfo ci) {
        AdvancementHelper.filterAdvancements(map);
    }
}
