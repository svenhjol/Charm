package svenhjol.charm.feature.animal_armor_grinding.common;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.feature.animal_armor_grinding.AnimalArmorGrinding;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<AnimalArmorGrinding> {
    public Advancements(AnimalArmorGrinding feature) {
        super(feature);
    }

    public void groundArmor(ServerPlayer player) {
        trigger("ground_animal_armor", player);
    }
}
