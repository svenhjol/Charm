package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class ClearItemFrames extends CommonFeature {
    static Supplier<SimpleParticleType> particleType;

    @Override
    public String description() {
        return "Add amethyst shards to item frames to make them invisible.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new CommonNetworking(this));
    }

    public static void triggerMadeClearItemFrame(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "made_clear_item_frame"), player);
    }
}
