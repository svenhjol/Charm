package svenhjol.charm.mixin.helper;

import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.helper.AdvancementHelper;

import java.util.HashMap;
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
    @Redirect(
        method = "add",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;",
            remap = false
        )
    )
    private HashMap<ResourceLocation, Advancement.Builder> hookAdd(Map<ResourceLocation, Advancement.Builder> map) {
        var newMap = new HashMap<>(map);
        AdvancementHelper.filterAdvancements(newMap);
        return Maps.newHashMap(newMap);
    }
}
