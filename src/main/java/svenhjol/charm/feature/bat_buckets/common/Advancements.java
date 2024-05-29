package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<BatBuckets> {
    public Advancements(BatBuckets feature) {
        super(feature);
    }

    public void capturedBat(Player player) {
        trigger("captured_bat", player);
    }

    public void usedBatBucket(Player player) {
        trigger("used_bat_bucket", player);
    }
}
