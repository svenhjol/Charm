package svenhjol.charm.feature.azalea_wood;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.custom_wood.CustomWood;
import svenhjol.charmony.feature.woodcutting.Woodcutting;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.LevelLoadEvent;
import svenhjol.charmony_api.iface.*;

import java.util.List;
import java.util.function.Supplier;

public class AzaleaWood extends CommonFeature implements
    IVariantBarrelProvider,
    IVariantBookshelfProvider,
    IVariantChestProvider,
    IVariantChestBoatProvider,
    IVariantChiseledBookshelfProvider,
    IVariantLadderProvider
{
    static Supplier<BlockSetType> blockSetType;
    static Supplier<WoodType> woodType;
    static IVariantWoodMaterial material;

    @Override
    public String description() {
        return "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.";
    }

    @Override
    public void register() {
        // Must register Charmony's woodcutting recipe serializer as a dependency or woodcutting recipes will fail.
        Woodcutting.registerDependency();

        material = AzaleaMaterial.AZALEA;
        blockSetType = mod().registry().blockSetType(material);
        woodType = mod().registry().woodType(material.getSerializedName(), material);

        CustomWood.registerWood(this, mod().registry(), new AzaleaWoodDefinition());

        CharmonyApi.registerProvider(this);
        CharmonyApi.registerProvider(new AzaleaWoodRecipeFilter());
    }

    @Override
    public void runWhenEnabled() {
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

    @Override
    public List<IVariantMaterial> getVariantBarrels() {
        return List.of(material);
    }

    @Override
    public List<IVariantMaterial> getVariantBookshelves() {
        return List.of(material);
    }

    @Override
    public List<IVariantMaterial> getVariantChests() {
        return List.of(material);
    }

    @Override
    public List<IVariantMaterial> getVariantChiseledBookshelves() {
        return List.of(material);
    }

    @Override
    public List<IVariantMaterial> getVariantLadders() {
        return List.of(material);
    }

    @Override
    public List<IVariantChestBoatDefinition> getVariantChestBoatDefinitions() {
        return List.of(new AzaleaChestBoatDefinition());
    }
}
