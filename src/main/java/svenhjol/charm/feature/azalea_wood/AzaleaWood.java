package svenhjol.charm.feature.azalea_wood;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.event.LevelLoadEvent;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantChestProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.block.CharmDoorBlock;
import svenhjol.charmony.block.CharmLogBlock;
import svenhjol.charmony.block.CharmTrapdoorBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmFeature
    implements IRecipeRemoveProvider, IVariantBarrelProvider, IVariantChestProvider {
    static Supplier<BlockSetType> BLOCK_SET_TYPE;
    static Supplier<WoodType> WOOD_TYPE;
    static Supplier<CharmDoorBlock> DOOR_BLOCK;
    static Supplier<CharmTrapdoorBlock> TRAPDOOR_BLOCK;
    static Supplier<CharmLogBlock> LOG_BLOCK;

    @Override
    public void register() {
        var material = AzaleaMaterial.AZALEA;
        var values = Boat.Type.values(); // TODO: Hack to inject the boat type enums early, fixme.
        var registry = Charm.instance().registry();

        BLOCK_SET_TYPE = registry.blockSetType(material);
        WOOD_TYPE = Wood.registerWoodType(registry, material);
        DOOR_BLOCK = Wood.registerDoor(registry, this, material).getFirst();
        TRAPDOOR_BLOCK = Wood.registerTrapdoor(registry, this, material).getFirst();
        LOG_BLOCK = Wood.registerLog(registry, this, material).get("azalea_log").getFirst();

        Wood.registerBoat(registry, this, material);

        Wood.registerButton(registry, this, material);
        Wood.registerFence(registry, this, material);
        Wood.registerGate(registry, this, material);

        Wood.registerPlanksSlabsAndStairs(registry, this, material);
        Wood.registerSign(registry, this, material);
        Wood.registerHangingSign(registry, this, material);
        Wood.registerPressurePlate(registry, this, material);

//        Wood.registerBarrel(registry, material);
        Wood.registerBookshelf(registry, material);
        Wood.registerChiseledBookshelf(registry, material);
//        Wood.registerChest(registry, material);
//        Wood.registerTrappedChest(registry, material);
        Wood.registerLadder(registry, material);

        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    @SuppressWarnings("unchecked")
    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        // Make naturally occurring azalea trees use Charm's azalea log.
        var configuredFeatures = server.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElseThrow();
        ConfiguredFeature<?, ?> feature
            = configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE);

        ((ConfiguredFeature<TreeConfiguration, ?>)feature).config().trunkProvider
            = new SimpleStateProvider(LOG_BLOCK.get().defaultBlockState());
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
        return List.of(AzaleaMaterial.AZALEA);
    }

    @Override
    public List<IVariantMaterial> getVariantChests() {
        return List.of(AzaleaMaterial.AZALEA);
    }
}
