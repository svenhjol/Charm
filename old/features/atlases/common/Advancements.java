package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.Advancement;

public final class Advancements extends Advancement<Atlases> {
    public static final int NUMBER_OF_MAPS_FOR_ACHIEVEMENT = 10;

    public void madeAtlasMaps(Player player) {
        trigger("made_atlas_maps", player);
    }

    @Override
    protected Class<Atlases> type() {
        return Atlases.class;
    }
}
