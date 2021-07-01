package svenhjol.charm.init;

import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.advancement.ActionPerformedCriterion;
import svenhjol.charm.loader.CommonLoader;
import svenhjol.charm.module.core.Core;

import java.util.ArrayList;
import java.util.List;

public class CharmAdvancements {
    public static ActionPerformedCriterion ACTION_PERFORMED;
    public static final List<ResourceLocation> modulesToRemove = new ArrayList<>();

    public static void init() {
        ACTION_PERFORMED = CriteriaAccessor.callRegister(new ActionPerformedCriterion());
    }

    public static void processModules() {
        modulesToRemove.clear();

        CommonLoader.getAllModules().values().forEach(module -> {
            if (!module.isEnabled() || !Core.advancements)
                modulesToRemove.add(module.getId());
        });
    }
}
