package svenhjol.charm.feature.azalea_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.foundation.feature.FeatureHolder;

public class Handlers extends FeatureHolder<AzaleaWood> {
    public Handlers(AzaleaWood feature) {
        super(feature);
    }

    @SuppressWarnings({"unchecked", "unused"})
    public void levelLoad(MinecraftServer server, ServerLevel level) {
        var holder = CustomWood.holder(feature().registers.material.get());
        var log = holder.log().orElseThrow();

        // Make naturally occurring azalea trees use Charm's azalea log.
        var configuredFeatures = server.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElseThrow();
        ConfiguredFeature<?, ?> feature
            = configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE);

        ((ConfiguredFeature<TreeConfiguration, ?>)feature).config().trunkProvider
            = new SimpleStateProvider(log.block.get().defaultBlockState());
    }
}
