package svenhjol.charm.mixin.feature.core.custom_advancements;

import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;

import java.util.HashMap;
import java.util.Map;

@Mixin(AdvancementList.class)
public class AdvancementTreeMixin {
    public AdvancementTreeMixin() {
    }

    @Redirect(
            method = {"add"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;",
                    remap = false
            )
    )
    private HashMap<ResourceLocation, Advancement.Builder> hookAdd(Map<ResourceLocation, Advancement.Builder> map) {
        Map<ResourceLocation, Advancement.Builder> newMap = new HashMap<>();
        map.forEach((id, builder) -> {
            if (Resolve.feature(CustomAdvancements.class).handlers.shouldRemove(id)) {
                newMap.put(id, builder);
            }

        });
        return Maps.newHashMap(newMap);
    }
}
