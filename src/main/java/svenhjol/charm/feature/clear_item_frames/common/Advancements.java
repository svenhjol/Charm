package svenhjol.charm.feature.clear_item_frames.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.clear_item_frames.ClearItemFrames;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ClearItemFrames> {
    public Advancements(ClearItemFrames feature) {
        super(feature);
    }

    public void madeClearItemFrame(Player player) {
        trigger("made_clear_item_frame", player);
    }
}
