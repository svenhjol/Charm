package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomWood extends CommonFeature {
    static final Map<IVariantWoodMaterial, CustomWoodHolder> REGISTERED_WOOD = new HashMap<>();

    @Override
    public String description() {
        return "Handles custom wood.";
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }

    public static Map<IVariantWoodMaterial, CustomWoodHolder> getHolders() {
        return REGISTERED_WOOD;
    }

    /**
     * Called by mods.
     */
    @SuppressWarnings("unused")
    public static CustomWoodHolder getHolder(IVariantWoodMaterial material) {
        return Optional.of(REGISTERED_WOOD.get(material)).orElseThrow();
    }

    /**
     * Called by mods.
     */
    @SuppressWarnings("unused")
    public static void registerWood(CommonRegistry registry, CustomWoodDefinition definition) {
        var holder = new CustomWoodHolder(registry, definition);
        REGISTERED_WOOD.put(definition.getMaterial(), holder);
    }
}
