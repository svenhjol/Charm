package svenhjol.charm.feature.revive_pets;

import svenhjol.charm.feature.revive_pets.common.Advancements;
import svenhjol.charm.feature.revive_pets.common.Handlers;
import svenhjol.charm.feature.revive_pets.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    A named pet drops its name tag on death.
    Right-click (use) the name tag while holding a Totem of Undying to revive the pet and consume the totem.""")
public final class RevivePets extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(
        name = "Drop loot on death",
        description = """
            If true, named pets drop items from their loot table (such as leather from horses) when they die.
            This could be unwanted behavior as it can be used to dupe items by killing and resurrecting a pet repeatedly."""
    )
    public static boolean dropLootOnDeath = false;

    @Configurable(
        name = "Drop experience on death",
        description = """
            If true, named pets drop experience when they die. This could be unwanted behavior as it can be used
            to receive an increasing amount experience by killing and resurrecting a pet repeatedly."""
    )
    public static boolean dropExperienceOnDeath = false;

    public RevivePets(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }
}
