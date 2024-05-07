package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.feature.totem_of_preserving.common.Advancements;
import svenhjol.charm.feature.totem_of_preserving.common.Handlers;
import svenhjol.charm.feature.totem_of_preserving.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

public class TotemOfPreserving extends CommonFeature {
    public static Advancements advancements;
    public static Registers registers;
    public static Handlers handlers;

    @Configurable(
        name = "Grave mode",
        description = "If true, a totem of preserving will always be created when you die.\n" +
            "If false, you must be holding a totem of preserving to preserve your items on death."
    )
    public static boolean graveMode = false;

    @Configurable(
        name = "Durability",
        description = """
            The maximum number of times a single totem can be used. Once a totem runs out of uses it is destroyed.
            A value of -1 means that the totem is never destroyed.
            You can add an echo shard on an anvil to increase the durability of the totem.
            Note: Durability has no effect if 'Grave mode' is enabled."""
    )
    public static int durability = 3;

    @Configurable(
        name = "Owner only",
        description = "If true, only the owner of the totem may pick it up.",
        requireRestart = false
    )
    public static boolean ownerOnly = false;

    @Configurable(
        name = "Show death position",
        description = "If true, the coordinates where you died will be added to the player's chat screen.",
        requireRestart = false
    )
    public static boolean showDeathPositionInChat = false;

    @Override
    public String description() {
        return "Preserves your items on death.";
    }

    @Override
    public void setup() {
        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
