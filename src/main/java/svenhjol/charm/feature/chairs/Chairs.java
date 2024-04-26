package svenhjol.charm.feature.chairs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.function.Supplier;

/**
 * Inspired by Quark's SitInStairs module.
 */
public class Chairs extends CommonFeature {
    static Supplier<EntityType<ChairEntity>> entity;

    @Override
    public String description() {
        return "Right-click (with empty hand) on any stairs block to sit down.";
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }

    public static void triggerSatOnChair(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "sat_on_chair"), player);
    }
}
