package svenhjol.charm.feature.azalea_wood;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<AzaleaWood> {
    public CommonRegister(AzaleaWood feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        var material = AzaleaMaterial.AZALEA;

        AzaleaWood.material = material;
        AzaleaWood.blockSetType = registry.blockSetType(material);
        AzaleaWood.woodType = registry.woodType(material.getSerializedName(), material);

        CustomWood.registerWood(registry, new AzaleaWoodDefinition());
        VariantWood.registerWood(registry, material);

        CharmApi.registerProvider(this);
        CharmApi.registerProvider(new AzaleaWoodRecipeProvider());
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    @SuppressWarnings({"unchecked", "unused"})
    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        var holder = CustomWood.getHolder(AzaleaWood.material);
        var log = holder.getLog().orElseThrow();

        // Make naturally occurring azalea trees use Charm's azalea log.
        var configuredFeatures = server.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElseThrow();
        ConfiguredFeature<?, ?> feature
            = configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE);

        ((ConfiguredFeature<TreeConfiguration, ?>)feature).config().trunkProvider
            = new SimpleStateProvider(log.block.get().defaultBlockState());
    }
}
