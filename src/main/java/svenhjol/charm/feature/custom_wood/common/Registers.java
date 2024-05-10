package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.HashMap;
import java.util.Map;

public final class Registers extends RegisterHolder<CustomWood> {
    public final Map<IVariantWoodMaterial, CustomWoodHolder> holders = new HashMap<>();

    public Registers(CustomWood feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }

    public void register(CommonFeature feature, CustomWoodDefinition definition) {
        holders.put(definition.getMaterial(), new CustomWoodHolder(feature, definition));
    }
}
