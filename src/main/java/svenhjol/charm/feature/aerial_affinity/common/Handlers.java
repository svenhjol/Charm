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
        var playerAttributes = player.getAttributes();

        if (!player.onGround() && playerAttributes.hasAttribute(aerialAffinity)) {
            var aerialAffinityValue = playerAttributes.getValue(aerialAffinity);

            // Weird hack, it's 0.2 when inactive.
            if (aerialAffinityValue > 0.2d) {
                log().dev("Aerial affinity value: " + aerialAffinityValue);
                feature().advancements.usedAerialAffinity(player);
                return currentSpeed * 5.0f; // Restores the speed penalty introduced in getDestroySpeed().
            }
        }

        return currentSpeed;
    }

}
