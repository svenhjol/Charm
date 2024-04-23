package svenhjol.charm.feature.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.foundation.Register;

public class RegisterCommon extends Register<Advancements> {
    public static final String CRITERIA_ID = "charmony_action_performed";

    public RegisterCommon(Advancements feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Advancements.actionPerformed = CriteriaTriggers.register(CRITERIA_ID, new ActionPerformed());
    }
}
