package svenhjol.charm.feature.collection;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class Collection extends CommonFeature {
    public static Supplier<Enchantment> enchantment;

    @Override
    public String description() {
        return "Tools with the Collection enchantment automatically pick up drops.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerUseCollection(ServerPlayer player) {
        Advancements.trigger(Charm.id("used_collection"), player);
    }
}