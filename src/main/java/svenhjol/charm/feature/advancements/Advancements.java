package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.ApiHelper;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public class Advancements extends CommonFeature {
    static ActionPerformed actionPerformed;
    static final List<String> FUZZY_REMOVE = new ArrayList<>();
    static final List<String> EXACT_REMOVE = new ArrayList<>();
    static final List<IConditionalAdvancement> CONDITIONS = new ArrayList<>();

    @Override
    public String description() {
        return """
            Filter advancements when Charmony-mod features or settings are disabled.
            Disabling this feature will cause unexpected behavior and potentially unachievable advancements.""";
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new RegisterCommon(this));
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> CONDITIONS.addAll(provider.getAdvancementConditions()));
    }

    public static void handlePackReload(String reason) {
        var log = Globals.common(Charm.ID).log();
        log.debug(Advancements.class, "Reloading Charm custom advancement filtering: " + reason);

        EXACT_REMOVE.clear();
        FUZZY_REMOVE.clear();

        for (var condition : CONDITIONS) {
            if (condition.test()) continue;
            condition.advancements().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    FUZZY_REMOVE.add(remove);
                } else {
                    EXACT_REMOVE.add(remove);
                }
            });
        }
    }

    public static boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, EXACT_REMOVE, FUZZY_REMOVE);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    @SuppressWarnings("unused")
    public static void trigger(ResourceLocation advancement, Player player) {
        // Don't do anything on the client.
        if (!player.level().isClientSide) {
            actionPerformed.trigger(advancement, (ServerPlayer)player);
        }
    }
}
