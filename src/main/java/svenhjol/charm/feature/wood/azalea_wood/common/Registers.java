package svenhjol.charm.feature.wood.azalea_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AzaleaWood> {
    public final Supplier<BlockSetType> blockSetType;
    public final Supplier<WoodType> woodType;
    public final Supplier<CustomWoodMaterial> material;

    public Registers(AzaleaWood feature) {
        super(feature);
        var registry = feature.registry();

        this.material = () -> Material.AZALEA;
        this.blockSetType = registry.blockSetType(material);
        this.woodType = registry.woodType(material);

        registry.runnable(() -> {
            CustomWood.register(feature, new WoodDefinition());
        });
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
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
