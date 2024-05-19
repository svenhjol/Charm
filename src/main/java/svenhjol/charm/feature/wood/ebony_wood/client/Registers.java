package svenhjol.charm.feature.wood.ebony_wood.client;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWoodClient;
import svenhjol.charm.feature.wood.ebony_wood.common.EbonyMaterial;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;

public final class Registers extends RegisterHolder<EbonyWoodClient> {
    public Registers(EbonyWoodClient feature) {
        super(feature);

        var registry = feature.registry();
        var holder = CustomWood.holder(EbonyMaterial.EBONY);

        var door = holder.door().orElseThrow();
        var leaves = holder.leaves().orElseThrow();
        var sapling = holder.sapling().orElseThrow();
        var trapdoor = holder.trapdoor().orElseThrow();

        // Cut out transparent areas.
        registry.blockRenderType(door.block, RenderType::cutout);
        registry.blockRenderType(sapling.block, RenderType::cutout);
        registry.blockRenderType(trapdoor.block, RenderType::cutout);

        // Foliage colors.
        registry.itemColor(List.of(leaves.block));
        registry.blockColor(List.of(leaves.block));
    }
}
