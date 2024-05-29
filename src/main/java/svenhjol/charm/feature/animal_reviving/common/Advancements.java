package svenhjol.charm.feature.animal_reviving.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<AnimalReviving> {
    public Advancements(AnimalReviving feature) {
        super(feature);
    }

    public void revivedAnimal(Player player) {
        trigger("revived_animal", player);
    }
}
