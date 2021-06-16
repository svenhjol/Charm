package svenhjol.charm.init;

import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import svenhjol.charm.advancement.ActionPerformedCriterion;
import svenhjol.charm.handler.AdvancementHandler;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.core.Core;

public class CharmAdvancements {
    public static ActionPerformedCriterion ACTION_PERFORMED;

    public static void init() {
        ACTION_PERFORMED = CriteriaAccessor.callRegister(new ActionPerformedCriterion());

        AdvancementHandler.modulesToRemove.clear();

        ModuleHandler.INSTANCE.getLoaders().forEach(loader -> loader.eachModule(m -> {
            if (!m.enabled || !Core.advancements)
                AdvancementHandler.modulesToRemove.add(m.getId());
        }));
    }
}
