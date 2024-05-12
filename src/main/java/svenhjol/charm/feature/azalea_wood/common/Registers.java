package svenhjol.charm.feature.azalea_wood.common;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
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
    public final Supplier<IVariantWoodMaterial> material;

    public Registers(AzaleaWood feature) {
        super(feature);
        var registry = feature.registry();

        this.material = () -> Material.AZALEA;
        this.blockSetType = registry.blockSetType(material);
        this.woodType = registry.woodType(material);

        registry.runnable(() -> {
            CustomWood.register(feature, new WoodDefinition());
            VariantWood.register(feature, material.get());
        });
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }
}
