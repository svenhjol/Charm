package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.totem_of_preserving.common.Advancements;
import svenhjol.charm.feature.totem_of_preserving.common.Handlers;
import svenhjol.charm.feature.totem_of_preserving.common.Providers;
import svenhjol.charm.feature.totem_of_preserving.common.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Preserves your items on death.")
public final class TotemOfPreserving extends CommonFeature {
    public final Advancements advancements;
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    @Configurable(
        name = "Grave mode",
        description = """
            If true, a totem of preserving will always be created when you die.
            If false, you must be holding a totem of preserving to preserve your items on death."""
    )
    private static boolean graveMode = false;

    @Configurable(
        name = "Durability",
        description = """
            The maximum number of times a single totem can be used. Once a totem runs out of uses it is destroyed.
            A value of -1 means that the totem is never destroyed.
            You can add an echo shard on an anvil to increase the durability of the totem.
            Note: Durability has no effect if 'Grave mode' is enabled."""
    )
    private static int durability = 3;

    @Configurable(
        name = "Owner only",
        description = "If true, only the owner of the totem may pick it up.",
        requireRestart = false
    )
    private static boolean ownerOnly = false;

    @Configurable(
        name = "Show death position",
        description = "If true, the coordinates where you died will be added to the player's chat screen.",
        requireRestart = false
    )
    private static boolean showDeathPositionInChat = false;

    public TotemOfPreserving(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
        providers = new Providers(this);
    }

    public boolean graveMode() {
        return graveMode;
    }

    public int durability() {
        return Mth.clamp(durability, -1, 1000);
    }

    public boolean ownerOnly() {
        return ownerOnly;
    }

    public boolean showDeathPositionInChat() {
        return showDeathPositionInChat;
    }

}
