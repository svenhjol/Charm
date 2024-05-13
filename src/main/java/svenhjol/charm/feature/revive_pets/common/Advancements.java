package svenhjol.charm.feature.revive_pets.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<RevivePets> {
    public Advancements(RevivePets feature) {
        super(feature);
    }

    public void revivedAnimal(Player player) {
        trigger("revived_animal", player);
    }
}
