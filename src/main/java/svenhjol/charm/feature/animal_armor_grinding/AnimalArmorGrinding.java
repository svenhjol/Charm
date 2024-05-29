package svenhjol.charm.feature.animal_armor_grinding;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.animal_armor_grinding.common.Advancements;
import svenhjol.charm.feature.animal_armor_grinding.common.Handlers;
import svenhjol.charm.feature.animal_armor_grinding.common.Providers;
import svenhjol.charm.feature.animal_armor_grinding.common.Registers;

@Feature(description = "Animal armor returns a single ingot, leather, scute or diamond when used on the grindstone.")
public final class AnimalArmorGrinding extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;
    public final Advancements advancements;

    public AnimalArmorGrinding(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
        advancements = new Advancements(this);
    }
}
