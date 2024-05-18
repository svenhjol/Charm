package svenhjol.charm.feature.villager_attracting.common;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<VillagerAttracting> {
    public Advancements(VillagerAttracting feature) {
        super(feature);
    }

    public void attractedVillager(ServerPlayer player) {
        trigger("attracted_villager", player);
    }
}
