package svenhjol.charm.helper;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class EntityHelper {
    public static Map<ModelLayerLocation, ModelPart> LAYERS = new HashMap<>();

    public static ModelLayerLocation registerEntityModelLayer(ResourceLocation id, ModelPart modelPart) {
        ModelLayerLocation layer = new ModelLayerLocation(id, "main");
        LAYERS.put(layer, modelPart);
        return layer;
    }
}
