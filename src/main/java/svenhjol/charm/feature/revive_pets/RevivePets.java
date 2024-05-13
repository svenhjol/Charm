package svenhjol.charm.feature.revive_pets;

import svenhjol.charm.feature.revive_pets.common.Advancements;
import svenhjol.charm.feature.revive_pets.common.Handlers;
import svenhjol.charm.feature.revive_pets.common.Registers;
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

    public RevivePets(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }
}
