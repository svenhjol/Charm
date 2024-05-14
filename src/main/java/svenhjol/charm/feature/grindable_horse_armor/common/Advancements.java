package svenhjol.charm.feature.grindable_horse_armor.common;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.feature.grindable_horse_armor.GrindableHorseArmor;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<GrindableHorseArmor> {
    public Advancements(GrindableHorseArmor feature) {
        super(feature);
    }

    public void recycledArmor(ServerPlayer player) {
        trigger("recycled_horse_armor", player);
    }
}
