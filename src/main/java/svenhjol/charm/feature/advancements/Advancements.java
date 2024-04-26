package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Advancements extends CommonFeature {
    static final List<IConditionalAdvancement> CONDITIONS = new ArrayList<>();
    static final Log LOGGER = new Log(Charm.ID, "Advancements");

    static ActionPerformed actionPerformed;

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
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> CONDITIONS.addAll(provider.getAdvancementConditions()));
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
