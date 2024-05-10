package svenhjol.charm.foundation.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.advancement.common.ActionPerformed;
import svenhjol.charm.foundation.advancement.common.Handlers;
import svenhjol.charm.foundation.helper.ApiHelper;

public class AdvancementManager {
    public static final Log LOGGER = new Log(Charm.ID, "Advancements");
    public final ActionPerformed actionPerformed;
    private static AdvancementManager instance;

    private AdvancementManager() {
        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());

        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> Handlers.CONDITIONS.addAll(provider.getAdvancementConditions()));
    }

    public static AdvancementManager instance() {
        if (instance == null) {
            instance = new AdvancementManager();
        }
        return instance;
    }
}
