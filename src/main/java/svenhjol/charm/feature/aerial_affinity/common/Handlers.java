package svenhjol.charm.feature.aerial_affinity.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.aerial_affinity.AerialAffinity;

public final class Handlers extends FeatureHolder<AerialAffinity> {
    public Handlers(AerialAffinity feature) {
        super(feature);
    }

    public float blockBreakSpeed(Player player, BlockState state, float currentSpeed) {
        var aerialAffinity = feature().registers.attribute.get();

        if (!player.onGround()) {
            var value = player.getAttributeValue(aerialAffinity);
                
            log().dev("Aerial affinity value: " + value);
            if (value > 0.0d) {
                feature().advancements.usedAerialAffinity(player);
                return currentSpeed * 5.0f; // Restores the speed penalty introduced in getDestroySpeed().
            }
        }

        return currentSpeed;
    }

}
