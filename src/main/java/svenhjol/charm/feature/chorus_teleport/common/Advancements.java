package svenhjol.charm.feature.chorus_teleport.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.chorus_teleport.ChorusTeleport;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ChorusTeleport> {
    public Advancements(ChorusTeleport feature) {
        super(feature);
    }

    public void teleportedToBlock(Player player) {
        trigger("teleported_to_block", player);
    }
}
