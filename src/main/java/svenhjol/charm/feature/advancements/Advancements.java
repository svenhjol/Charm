package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.common.Handlers;
import svenhjol.charm.feature.advancements.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;

public class Advancements extends CommonFeature {
    public static Registers registers;
    public static Handlers handlers;

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
    public void registers() {
        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    public static void trigger(ResourceLocation advancement, Player player) {
        if (handlers == null) {
            throw new RuntimeException("Advancement handlers not initialized");
        }
        Advancements.handlers.trigger(advancement, player);
    }
}
