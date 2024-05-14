package svenhjol.charm.feature.grindable_horse_armor;

import svenhjol.charm.feature.grindable_horse_armor.common.Advancements;
import svenhjol.charm.feature.grindable_horse_armor.common.Handlers;
import svenhjol.charm.feature.grindable_horse_armor.common.Providers;
import svenhjol.charm.feature.grindable_horse_armor.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Horse armor returns a single ingot, leather or diamond when used on the grindstone.")
public final class GrindableHorseArmor extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;
    public final Advancements advancements;

    public GrindableHorseArmor(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
        advancements = new Advancements(this);
    }
}
