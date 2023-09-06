package svenhjol.charm.feature.custom_wood;

import net.minecraft.core.registries.BuiltInRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.api.ICustomWoodDefinition;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.iface.ICommonRegistry;
import svenhjol.charmony.mixin.accessor.BlockItemAccessor;
import svenhjol.charmony.mixin.accessor.StandingAndWallBlockItemAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Handles custom wood.")
public class CustomWood extends CharmFeature {
    static final Map<IVariantWoodMaterial, CustomWoodHolder> REGISTERED_WOOD = new HashMap<>();

    @Override
    public void runWhenEnabled() {
        // Set each boat type's planks.
        CustomWoodHelper.getBoatPlanks().forEach(
            (type, id) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(
                block -> type.planks = block));

        // Set each sign's block.
        CustomWoodHelper.getSignItems().forEach(supplier -> {
            var sign = supplier.get();
            ((StandingAndWallBlockItemAccessor)sign).setWallBlock(sign.getWallSignBlock().get());
            ((BlockItemAccessor)sign).setBlock(sign.getSignBlock().get());
        });

        // Set each hanging sign's block.
        CustomWoodHelper.getHangingSignItems().forEach(supplier -> {
            var sign = supplier.get();
            ((StandingAndWallBlockItemAccessor)sign).setWallBlock(sign.getWallSignBlock().get());
            ((BlockItemAccessor)sign).setBlock(sign.getSignBlock().get());
        });
    }

    public static CustomWoodHolder getHolder(IVariantWoodMaterial material) {
        return Optional.of(REGISTERED_WOOD.get(material)).orElseThrow();
    }

    public static Map<IVariantWoodMaterial, CustomWoodHolder> getHolders() {
        return REGISTERED_WOOD;
    }

    public static void registerWood(CharmFeature feature, ICommonRegistry registry, ICustomWoodDefinition definition) {
        var holder = new CustomWoodHolder(feature, registry, definition);
        REGISTERED_WOOD.put(definition.getMaterial(), holder);
    }
}
