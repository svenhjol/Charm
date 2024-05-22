package svenhjol.charm.feature.crop_replanting.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.crop_replanting.CropReplanting;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<CropReplanting> {
    public Advancements(CropReplanting feature) {
        super(feature);
    }

    public void triggerReplantedCrops(Player player) {
        trigger("replanted_crops", player);
    }
}
