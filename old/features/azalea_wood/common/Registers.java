package svenhjol.charm.feature.azalea_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.feature.Register;

import java.util.function.Supplier;

public final class Registers extends Register<AzaleaWood> {
    public final Supplier<BlockSetType> blockSetType;
    public final Supplier<WoodType> woodType;
    public final IVariantWoodMaterial material;

    public Registers(AzaleaWood feature) {
        super(feature);

        var registry = feature.registry();
        var material = Material.AZALEA;

        this.material = material;
        this.blockSetType = registry.blockSetType(material);
        this.woodType = registry.woodType(material.getSerializedName(), material);

        CustomWood.registerWood(registry, new WoodDefinition());
        VariantWood.registerWood(registry, material);

        CharmApi.registerProvider(this);
        CharmApi.registerProvider(new DataProviders());
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    @SuppressWarnings({"unchecked", "unused"})
    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        var holder = CustomWood.getHolder(material);
        var log = holder.getLog().orElseThrow();

        // Make naturally occurring azalea trees use Charm's azalea log.
        var configuredFeatures = server.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElseThrow();
        ConfiguredFeature<?, ?> feature
            = configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE);

        ((ConfiguredFeature<TreeConfiguration, ?>)feature).config().trunkProvider
            = new SimpleStateProvider(log.block.get().defaultBlockState());
    }
}
