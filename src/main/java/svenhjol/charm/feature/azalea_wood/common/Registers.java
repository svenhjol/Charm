package svenhjol.charm.feature.azalea_wood.common;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AzaleaWood> {
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

        CustomWood.register(feature, new WoodDefinition());
        VariantWood.register(feature, material);

        CharmApi.registerProvider(feature);
        CharmApi.registerProvider(new DataProviders());
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }
}
