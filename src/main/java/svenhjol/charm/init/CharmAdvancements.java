package svenhjol.charm.init;

import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import svenhjol.charm.advancement.ActionPerformedCriterion;

public class CharmAdvancements {
    public static ActionPerformedCriterion ACTION_PERFORMED;

    public static void init() {
        ACTION_PERFORMED = CriteriaAccessor.callRegister(new ActionPerformedCriterion());
    }
}
