package svenhjol.charm.feature.collection.common;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Collection> {
    public Advancements(Collection feature) {
        super(feature);
    }

    public void usedCollection(ServerPlayer player) {
        trigger("used_collection", player);
    }
}
