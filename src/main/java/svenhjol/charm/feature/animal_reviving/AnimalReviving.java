package svenhjol.charm.feature.animal_reviving;

import svenhjol.charm.feature.animal_reviving.common.Advancements;
import svenhjol.charm.feature.animal_reviving.common.Handlers;
import svenhjol.charm.feature.animal_reviving.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    A tamed animal with a name drops its name tag on death.
    Right-click (use) the name tag while holding a Totem of Undying to revive the animal and consume the totem.""")
public final class AnimalReviving extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(
        name = "Drop loot on death",
        description = """
            If true, tamed and named animals drop items from their loot table (such as leather from horses) when they die.
            This could be unwanted behavior as it can be used to dupe items by killing and resurrecting an animal repeatedly."""
    )
    private static boolean dropLootOnDeath = false;

    @Configurable(
        name = "Drop experience on death",
        description = """
            If true, tamed and named animals drop experience when they die. This could be unwanted behavior as it can be used
            to receive an increasing amount experience by killing and resurrecting an animal repeatedly."""
    )
    private static boolean dropExperienceOnDeath = false;

    public AnimalReviving(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }

    public boolean dropLootOnDeath() {
        return dropLootOnDeath;
    }

    public boolean dropExperienceOnDeath() {
        return dropExperienceOnDeath;
    }
}
