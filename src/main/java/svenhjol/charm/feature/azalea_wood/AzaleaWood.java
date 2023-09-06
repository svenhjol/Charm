package svenhjol.charm.feature.azalea_wood;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.api.*;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.event.LevelLoadEvent;
import svenhjol.charmony.api.iface.*;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmFeature implements
    IRecipeRemoveProvider,
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

    @SuppressWarnings("unused")
    @Override
    public void preRegister() {
        // TODO: Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();
    }

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        material = AzaleaMaterial.AZALEA;
        blockSetType = registry.blockSetType(material);
        woodType = registry.woodType(material.getSerializedName(), material);

        CharmonyApi.registerProvider(this);
        CustomWood.registerWood(this, registry, new AzaleaWoodDefinition());
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
    public List<ResourceLocation> getRecipesToRemove() {
        var charm = Charm.instance();
        List<ResourceLocation> remove = new ArrayList<>();

        if (!charm.loader().isEnabled(VariantBarrels.class)) {
            remove.add(charm.makeId("azalea_barrel"));
        }

        if (!charm.loader().isEnabled(VariantChests.class)) {
            remove.add(charm.makeId("azalea_chest"));
            remove.add(charm.makeId("azalea_trapped_chest"));
        }

        if (!charm.loader().isEnabled(VariantLadders.class)) {
            remove.add(charm.makeId("azalea_ladder"));
        }

        if (!charm.loader().isEnabled("Woodcutters")) {
            remove.add(charm.makeId("azalea_wood/woodcutting/"));
        }

        return remove;
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
    public List<Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>> getVariantChestBoatPairs() {
        var holder = CustomWood.getHolder(material).getBoat().orElseThrow();

        return List.of(
            Pair.of(holder.boat, holder.chestBoat)
        );
    }

    @Override
    public List<IVariantWoodMaterial> getVariantChestLayerColors() {
        return List.of(material);
    }
}
