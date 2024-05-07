package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.common.Handlers;
import svenhjol.charm.feature.advancements.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.Setup;

public class Advancements extends CommonFeature {
    public final Setup<Registers> registers = Setup.create(this, Registers::new);
    public final Setup<Handlers> handlers = Setup.create(this, Handlers::new);

    public Advancements(CommonLoader loader) {
        super(loader);
    }

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

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     * Deprecation: Create a feature setup Advancement and use the parent's trigger() method.
     */
    @Deprecated
    public static void trigger(ResourceLocation advancement, Player player) {
        Resolve.support(Handlers.class).trigger(advancement, player);
    }
}
