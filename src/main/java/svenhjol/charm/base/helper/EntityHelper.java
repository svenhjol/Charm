package svenhjol.charm.base.helper;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class EntityHelper {
    public static Map<EntityModelLayer, ModelPart> LAYERS = new HashMap<>();

    public static EntityModelLayer registerEntityModelLayer(Identifier id, ModelPart modelPart) {
        EntityModelLayer layer = new EntityModelLayer(id, "main");
        LAYERS.put(layer, modelPart);
        return layer;
    }
}
