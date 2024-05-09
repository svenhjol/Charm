package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.common.Handlers;
import svenhjol.charm.feature.advancements.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Filter advancements when Charmony-mod features or settings are disabled.
    Disabling this feature will cause unexpected behavior and potentially unachievable advancements.""",
    priority = 10
)
public class Advancements extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public Advancements(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     * Deprecation: Create a feature setup Advancement and use the parent's trigger() method.
     */
    @Deprecated
    public static void trigger(ResourceLocation advancement, Player player) {
        Resolve.feature(Advancements.class).handlers.trigger(advancement, player);
    }
}
