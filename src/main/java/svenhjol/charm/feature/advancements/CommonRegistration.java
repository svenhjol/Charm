package svenhjol.charm.feature.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.foundation.Registration;

public final class CommonRegistration extends Registration<Advancements> {
    public static final String CRITERIA_ID = "charmony_action_performed";

    public CommonRegistration(Advancements feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Advancements.actionPerformed = CriteriaTriggers.register(CRITERIA_ID, new ActionPerformed());
    }
}
